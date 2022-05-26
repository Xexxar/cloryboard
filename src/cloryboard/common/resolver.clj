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
f
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
        ((easings/easings (get effect :easing)) (/ (- time start) end)))))

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
            (maths/vec-add acc args))))
    start-value
    (reduce-functions-to-last-absolute-reset functions polarity)))

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
            (maths/vec-multiply-vectors acc args))))
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
            (maths/vec-multiply-vectors acc args))))
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
            (maths/vec-multiply-vectors acc args))))
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
            (maths/vec-add [(* (- 1 percent) (get args 0)) (* (- 1 percent) (get args 1)) (* (- 1 percent) (get args 2))]
                           [(* percent (get args 3)) (* percent (get args 4)) (* percent (get args 5))])
            (maths/vec-add acc args))))
    start-value
    (reduce-functions-to-last-absolute-reset functions polarity))))

;; okay positionally resetting effects are viewed as chronologically bound to
;; functional order within the :function list, meaning that if I have a movement
;; function listed after a positionally resetting movement, i would apply the
;; new effect's net movement on the positional reset, moving the reset in time.
;; Basically, resets are only overwriting the previous functions. they themselves
;; can be overwritten by new functions

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



(defn- grand-resolver
  "Generic handler for any function group that resolves a block of functions
  with different easings into a single stream of form compliant functions."
  [metadata functions]
  (let [effect (get-in functions [0 :function])]
    (cond
      (nil? effect) ;; function isn't being used.
        functions
      (= "M" effect)
        (resolve-function metadata functions
          )))))

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
          [(grand-movement-resolver
            (assoc metadata :position (get object :position))
            (get function-groups "M"))
           (grand-fade-resolver metadata (get function-groups "F"))
           (grand-scale-resolver metadata (get function-groups "S"))
           (grand-rotation-resolver metadata (get function-groups "R"))
           (grand-color-resolver metadata (get function-groups "C")) ;; I hope I never have to use this.
           (grand-vector-scale-resolver metadata (get function-groups "V"))]))))
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
