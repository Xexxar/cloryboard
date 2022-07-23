(ns cloryboard.common.resolver
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.common.easings :as easings]
            [cloryboard.common.maths :as maths]
            [clojure.java.io :as io]))

(defn resolve-function-timing
  "Converts fractional timing into MS timing based off :metadata :start and :end
  time"
  [objects]
  (mapv
    (fn [obj] ;;for object in objects
      (let [start (get-in obj [:metadata :start])
            end (get-in obj [:metadata :end])
            duration (- end start)]
        (assoc obj :functions
          (mapv
            (fn [f] ;;for functions in object
              (let [time-delta (get-in f [:metadata :time-delta])]
              (-> f
                  (update :start (fn [frac] (if (some? frac) (int (maths/sum-vars-ignore-nil start (* frac duration) time-delta)) nil)))
                  (update :end (fn [frac] (if (some? frac) (int (maths/sum-vars-ignore-nil start (* frac duration) time-delta)) nil))))))
            (get obj :functions)))))
    objects))

(defn- reduce-functions-to-last-absolute-reset
  "I filter down your active functions to only those active after the last
  'absolute reset' which is when your arguments polarity is = 'x' where 'x' is
  the full :arguments length of a :function. otherwise I return the entire
  functions list."
  [functions polarity]
  (let [last-pol (last (filter #(= polarity (count (get % :arguments))) functions))]
    (if (nil? last-pol)
      functions
      (reduce (fn [acc elm]
        (if
          (or (= elm last-pol) (not (empty? acc)))
            (conj acc elm)
            acc))
        []
        functions))))

(defn- calc-percent-elapsed
  "Give me an effect and a time and I'll calculate the percent elapsed based on
  the easing used. Range is [0 1]."
  [effect time]
  (let [start (get effect :start)
        end (get effect :end)]
    (cond ;; yeah I could just clamp (/ (- time start) end). This is full retard mode.
      (<= end time)
        1
      (<= time start)
        0
      :else
        ((easings/easings (get effect :easing)) (/ (- time start) (- end start))))))

(defn- movement-resolver-function
  "Takes every active function and the time and calculates the end position at
  time"
  [functions time start-value]
  (let [polarity 4]
    (reduce
      (fn [acc elm]
        (let [args (get elm :arguments)
              percent (calc-percent-elapsed elm time)]
          (if (= polarity (count args))
            (maths/vec-add [(* (- 1 percent) (get args 0)) (* (- 1 percent) (get args 1))]
                           [(* percent (get args 2)) (* percent (get args 3))])
            (maths/vec-add acc [(* percent (get args 0)) (* percent (get args 1))]))))
      start-value
      (reduce-functions-to-last-absolute-reset functions polarity))))

(defn- fade-scale-resolver-function
  "Takes every active function and the time and calculates the scale or fade at
   time"
  [functions time start-value]
  (let [polarity 2]
    (reduce
      (fn [acc elm]
        (let [args (get elm :arguments)
              percent (calc-percent-elapsed elm time)]
          (if (= polarity (count args))
            (maths/vec-add [(* (- 1 percent) (get args 0))]
                           [(* percent (get args 1))])
            (maths/vec-multiply-vectors acc [(* percent (get args 0))]))))
    start-value
    (reduce-functions-to-last-absolute-reset functions polarity))))

(defn- vectorscale-resolver-function
  "Takes every active function and the time and calculates the vectorscale at
   time"
  [functions time start-value]
  (let [polarity 4]
    (reduce
      (fn [acc elm]
        (let [args (get elm :arguments)
              percent (calc-percent-elapsed elm time)]
          (if (= polarity (count args))
            (maths/vec-add [(* (- 1 percent) (get args 0)) (* (- 1 percent) (get args 1))]
                           [(* percent (get args 2)) (* percent (get args 3))])
            (maths/vec-multiply-vectors acc [(* percent (get args 0)) (* percent (get args 1))]))))
    start-value
    (reduce-functions-to-last-absolute-reset functions polarity))))

(defn- color-resolver-function
  "Takes every active function and the time and calculates the color multiplier
   at time"
  [functions time start-value]
  (let [polarity 6]
    (reduce
      (fn [acc elm]
        (let [args (get elm :arguments)
              percent (calc-percent-elapsed elm time)]
          (if (= polarity (count args))
            (maths/vec-add [(* (- 1 percent) (get args 0)) (* (- 1 percent) (get args 1)) (* (- 1 percent) (get args 2))]
                           [(* percent (get args 3)) (* percent (get args 4)) (* percent (get args 5))])
            (maths/vec-multiply-vectors acc [(* percent (get args 0)) (* percent (get args 1)) (* percent (get args 2))]))))
    start-value
    (reduce-functions-to-last-absolute-reset functions polarity))))

(defn- rotation-resolver-function
  "Takes every active function and the time and calculates the rotation at time"
  [functions time start-value]
  (let [polarity 2]
    (reduce
      (fn [acc elm]
        (let [args (get elm :arguments)
              percent (calc-percent-elapsed elm time)]
          (if (= polarity (count args))
            (maths/vec-add [(* (- 1 percent) (get args 0))]
                           [(* percent (get args 1))])
            (maths/vec-add acc [(* percent (get args 0))]))))
    start-value
    (reduce-functions-to-last-absolute-reset functions polarity))))

(defn- get-start-value
  "Gets the start value for the specific effect. Position for 'M', etc..."
  [object effect]
  (cond
    (= "F" effect)
      [1.0]
    (= "S" effect)
      [1.0]
    (= "C" effect)
      [1.0 1.0 1.0]
    (= "V" effect)
      [1.0 1.0]
    (= "R" effect)
      [0.0]
    (= "M" effect)
      (get object :position)))

(defn- calc-current-effect-at-time
  "I am a utility function that is provided a 'resolver-function' which allows
  me to process any 'function-type' since the function is provided by previous
  call. I assume you have reduced the functions list to only the specific
  effect you want, otherwise I'll blow up :^). I'm engine specific code and if
  you're messing with me you're probably having a bad time anyway."
  [object time effect effect-function]
  (let [active-functions (filterv #(<= (get % :start) time) (get object :functions))
        start-value (get-start-value object effect)]
    (if
      (empty? active-functions)
        start-value
        (effect-function active-functions time start-value))))

(defn get-current-effect-value
  "Got an object you want to know the current effect value at a specific time?
  Say no more fam. I will handle which resolver-function you need and will do
  the necessary calculations."
  [object time effect]
  (let [functions (filterv #(= effect (get % :function)) (get object :functions))]
    (cond
      (nil? functions)
        []
      (= "M" effect)
        (calc-current-effect-at-time
          (assoc object :functions functions)
          time
          "M"
          movement-resolver-function)
      (= "R" effect)
        (calc-current-effect-at-time
          (assoc object :functions functions)
          time
          "R"
          rotation-resolver-function)
      (= "S" effect)
        (calc-current-effect-at-time
          (assoc object :functions functions)
          time
          "S"
          fade-scale-resolver-function)
      (= "C" effect)
        (calc-current-effect-at-time
          (assoc object :functions functions)
          time
          "C"
          color-resolver-function)
      (= "F" effect)
        (calc-current-effect-at-time
          (assoc object :functions functions)
          time
          "F"
          fade-scale-resolver-function)
      (= "V" effect)
        (calc-current-effect-at-time
          (assoc object :functions functions)
          time
          "V"
          vectorscale-resolver-function))))

(defn- build-fraction-windows
  "Takes a list of fractions and concats them in start end pairs."
  [fractions]
  (mapv
    (fn [ind]
      [(get fractions ind) (get fractions (inc ind))])
    (range (dec (count fractions)))))

(defn- gather-fraction-timing-vector
  "Command that produces all independent fractions in order."
  [functions]
  (vec
    (sort
      (filterv some?
        (into #{}
          (reduce into
            (mapv (fn [elm]
              [(get elm :start) (get elm :end)])
              functions)))))))

(defn- sub-resolver
  "Figures out what movements are needed by the window"
  [metadata object resolver-function window effect]
  (let [active-functions (filterv #(<= (get % :start) (get window 1)) (get object :functions))
        window-functions (filterv #(and (< (get % :start) (get window 1))  (> (get % :end) (get window 0))) (get object :functions))
        start-value (get-start-value object effect)
        non-linear-easings (filterv #(not= (get % :easing) 0) window-functions)]
    (if
      (<= (count non-linear-easings) 1)
      [{:function effect
        :easing (if (= (count non-linear-easings) 1) (get-in non-linear-easings [0 :easing]) 0)
        :start (get window 0)
        :end (get window 1)
        :arguments (reduce conj (resolver-function active-functions (get window 0) start-value)
                                (resolver-function active-functions (get window 1) start-value))}]
      (reduce
        (fn [acc window]
          (conj acc
            {:function effect
             :easing 0
             :start (get window 0)
             :end (get window 1)
             :arguments (reduce conj (resolver-function active-functions (get window 0) start-value)
                                     (resolver-function active-functions (get window 1) start-value))}))
          []
          (let [easing-window 1/128 ;; TODO might want to look at 1/64 being the default or something else, either way ramer optimizes this.
                window-duration (- (get window 1) (get window 0))
                subparts (/ window-duration easing-window)
                times (if (integer? subparts) (conj (mapv #(maths/sum-vars-ignore-nil (get window 0) (* % easing-window)) (range subparts)) (get window 1)))
                sub-windows (build-fraction-windows times)]
            sub-windows
                )))))

(defn- grand-resolver
  "Generic handler for any function group that resolves a block of functions
  with different easings into a single stream of form compliant functions."
  [metadata object effect]
  (let [resolver-function (cond
                            (= "M" effect)
                              movement-resolver-function
                            (= "R" effect)
                              rotation-resolver-function
                            (= "S" effect)
                              fade-scale-resolver-function
                            (= "C" effect)
                              color-resolver-function
                            (= "F" effect)
                              fade-scale-resolver-function
                            (= "V" effect)
                              vectorscale-resolver-function)
        fraction-timing-list (gather-fraction-timing-vector (get object :functions))
        time-windows (if (> (count fraction-timing-list) 1)
                      (build-fraction-windows fraction-timing-list)
                      [[0 1]])]
    (if (not (empty? (get object :functions)))
      (reduce
        (fn [acc window]
          (reduce conj acc
            (sub-resolver metadata object resolver-function window effect)))
         []
         time-windows)
         [])))

(defn- grand-sovereign-supreme-master-general-resolver
  "A needlessly long titled function responsible for chronologically and
  systematically interlacing overlapping function calls to produce the natural
  combined effect for each basic function, F, S, M, C, R, V."
  [metadata objects]
  (mapv
    (fn [object]
      (assoc object :functions
      (let [function-groups (group-by :function (get object :functions))]
        (reduce into
          [(grand-resolver metadata (assoc object :functions (get function-groups "M")) "M")
           (grand-resolver metadata (assoc object :functions (get function-groups "F")) "F")
           (grand-resolver metadata (assoc object :functions (get function-groups "S")) "S")
           (grand-resolver metadata (assoc object :functions (get function-groups "R")) "R")
           (grand-resolver metadata (assoc object :functions (get function-groups "C")) "C")
           (grand-resolver metadata (assoc object :functions (get function-groups "V")) "V")]))))
    objects))


;; general notation. Any arguments map that contains half the arguments is a 'delta' vector.

;; S = [0.5]: reduce scale by 50%
;; M = [100 100]: move sprite by vectors coords
;; F = [0.5]: reduce current fade by 50%
;; R = [pi]: rotate image by +pi
;; V = [0.5 2]: scale image X by 50%, scale image Y by 100%
;; C = [0 -0.5 1]: multiplies the current C value by the decimal used, rounding up.

;; any argument map that contains full arguments is a discontinuous movement.
;; S = [0.75 0.5]: snap scale to 0.75, reduce scale to 50%
;; M = [200 100 200 100]: snap sprite back to [200 200], then move to [100 100] vectors coords
;; F = [0.5 0.75]: snap fade to 50%, raise to 75%.
;; R = [pi 0]: snap rotation to pi, rotate back to 0.
;; V = [0.5 1 2 1]: snap image to 0.5, 2.0 scale, and move back to 1:1.
;; C = [1 1 1 1 1 0]: reduce color yellow

;; when considering M,
;; First I need to take functions and interlace them into their 'effective' movement.
;; once I have that effective movement, then I can resolve those 'effective' movements
;; into actual movements, then optimize.

;; when considering S, F, R
;; F and S are multiplicative changes.

(defn apply-functions-to-objects
  [objects functions metadata]
  ;; TODO apply loop optimizer to loop commands that can be looped. (fun alg)
  ;; TODO apply ramer to optimize the movements to reduce lines.
  (grand-sovereign-supreme-master-general-resolver
    metadata
    (reduce (fn [acc func] (func acc)) objects functions)))


;; Resolvers job is to return the actual movement data.
;; Resolver has to resolve each of the
