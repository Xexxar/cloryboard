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




(defn- build-fraction-windows
  "Takes a list of fractions and concats them in start end pairs."
  [fractions]
  (mapv
    (fn [ind]
      [(get fractions ind) (get fractions (inc ind))])
    (range (dec (count fractions)))))

(defn gather-fraction-timing-vector
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

(defn gather-active-functions-for-fraction-window
  [functions time-period]
  (filterv
    #(and (<= (get % :start) (get time-period 0)) (>= (get % :end) (get time-period 1)))
    functions))

(defn fractionize?
  "Determines whether or not an easing approximation is needed."
  [active-functions window]
  (cond
    (< 1 (count (disj (set (mapv #(get % :easing) active-functions)) 0)))
      true
    (not ;; if you exist, then we need fractionalize
      (empty?
        (filterv ;; does your start stop time differ from the window?
          #(or (not= (get % :start) (get window 0)) (not= (get % :end) (get window 1)))
          (filterv #(not= (get % :easing) 0) active-functions)))) ;; for non linear-easings
      true
    :else
      false))

(defn resolve-movement-delta-for-window
  [active-functions start end]
  (reduce maths/vec-add
    (mapv
      (fn [function]
        (let [easing (get function :easing)
              easing-duration (- (get function :end) (get function :start))
              f-start-percent (easings/easing-to-value easing (/ (- start (get function :start)) easing-duration))
              f-end-percent (easings/easing-to-value easing (/ (- end (get function :start)) easing-duration))
              x-movement (- (get-in function [:arguments 2]) (get-in function [:arguments 0]))
              y-movement (- (get-in function [:arguments 3]) (get-in function [:arguments 1]))]
          [(- (* f-end-percent x-movement) (* f-start-percent x-movement))
           (- (* f-end-percent y-movement) (* f-start-percent y-movement))]))
    active-functions)))

(defn discontinuous-function?
  [functions window]
  (some?
    (some
      #(and (get-in % [:metadata :discontinous]) (= (get window 0) (get % :start)))
      functions)))
  ; (contains? (set (mapv
  ;   #(and (get-in % [:metadata :discontinous]) (= (get window 0) (get % :start)))
  ;   functions)) true))

(defn get-discontinuous-position
  [functions window]
  (let [disc-func (filterv
                    #(and (get-in % [:metadata :discontinous])
                          (= (get window 0) (get % :start)))
                    functions)]
    (if (< 1 (count disc-func))
      (throw (Exception. "Two discontinuous functions on the same window, cannot resolve start position for movement."))
      (let [args (get-in disc-func [0 :arguments])]
        [(get args 0) (get args 1)]))))

(defn create-m-function-for-window
  [active-functions last-position window]
  (let [e (first (disj (set (mapv #(get % :easing) active-functions)) 0))
        easing (if (nil? e) 0 e)
        movement-delta (resolve-movement-delta-for-window active-functions (get window 0) (get window 1))]
    {:function "M"
     :start (get window 0)
     :end (get window 1)
     :easing easing
     :arguments [(get last-position 0)
                 (get last-position 1)
                 (maths/sum-vars-ignore-nil (get last-position 0) (get movement-delta 0))
                 (maths/sum-vars-ignore-nil (get last-position 1) (get movement-delta 1))]}))

(defn create-m-functions-for-windows
  [active-functions windows last-position acc index]
  (if
    (>= index (count windows))
      acc
      (let [movement-delta (resolve-movement-delta-for-window active-functions
                                                              (get-in windows [index 0])
                                                              (get-in windows [index 1]))]
        (create-m-functions-for-windows
          active-functions
          windows
          (maths/vec-add last-position movement-delta)
          (conj acc
            {:function "M"
             :start (get-in windows [index 0])
             :end (get-in windows [index 1])
             :easing 0
             :arguments [(get last-position 0)
                         (get last-position 1)
                         (maths/sum-vars-ignore-nil (get last-position 0) (get movement-delta 0))
                         (maths/sum-vars-ignore-nil (get last-position 1) (get movement-delta 1))]})
          (inc index)))))

(defn movement-easing-fractionizer
  "Takes a specific time period and resolves a conflicting easings by breaking
  movement down into linear sub-movements based off the metadata param for the
  minimum effect-fraction. I.E: if you have two conflicting easings from 1/4 to
  3/4, and the min fraction is 1/64, you'll produce 32 submovements."
  [metadata last-position active-functions window]
  (let [easing-window (get metadata :m-easing)
        window-duration (- (get window 1) (get window 0))
        subparts (/ window-duration easing-window)]
    (if (integer? subparts)
      (let [times (conj (mapv #(maths/sum-vars-ignore-nil (get window 0) (* % easing-window)) (range subparts)) (get window 1))
            sub-windows (build-fraction-windows times)]
        (create-m-functions-for-windows
          active-functions sub-windows last-position [] 0))
      (throw (Exception. "I don't want to handle your fractions automatically right now, you have a bad :m-easing-window.. figure it out.")))))

(defn resolve-movement-for-period
  "Takes acculumator, finds last position, calcs new position, resolves any
  easing conflicts with the easing-fractionizer."
  [metadata last-function active-functions window]
  (let [lp (vec (rest (rest (get last-function :arguments))))
        last-position (if (empty? lp) (get metadata :position) lp)]
    (cond
      (and (fractionize? active-functions window) (discontinuous-function? active-functions window))
        (movement-easing-fractionizer metadata (get-discontinuous-position active-functions) active-functions window)
      (fractionize? active-functions window)
        (movement-easing-fractionizer metadata last-position active-functions window)
      (discontinuous-function? active-functions window)
        [(create-m-function-for-window active-functions (get-discontinuous-position active-functions) window)]
      :else
        [(create-m-function-for-window active-functions last-position window)])))

(defn grand-movement-resolver
  "Sovereign M function resolver."
  [metadata functions]
  (let [fraction-timing-list (gather-fraction-timing-vector functions)
        time-windows (if (> (count fraction-timing-list) 1)
                      (build-fraction-windows fraction-timing-list)
                      [[0 1]])] ;; If theres only 1 time stamp or less for this function, assume whole duration.
    (reduce ;; Rebuild functions based off time-windows.
      (fn [acc window]
        (let [functions-during-window (gather-active-functions-for-fraction-window functions window)]
          (if (empty? functions-during-window)
            acc
            (reduce conj acc (resolve-movement-for-period metadata (last acc) functions-during-window window)))))
      [] time-windows)))

(defn grand-scale-resolver
  "Sovereign S function resolver."
  [metadata functions]
  (identity functions))

(defn grand-vector-scale-resolver
  "Sovereign V function resolver."
  [metadata functions]
  (identity functions))

(defn grand-color-resolver
  "Sovereign M function resolver."
  [metadata functions]
  (identity functions))

(defn grand-rotation-resolver
  "Sovereign M function resolver."
  [metadata functions]
  (identity functions))

(defn grand-fade-resolver
  "Sovereign F function resolver."
  [metadata functions]
  (identity functions))

;; I need meta information for this resolving, particularly, I need to know the
;; level of desired easing approximation by fraction...
;; Default to effects smallest fraction gap. (1/GCM?)

; {:F-easing 1/8
 ; :S-easing 1/8} ... etc

; {:metadata
;   {:discontinous true}} ;; Defines a function that breaks continuity, allowing for discontinuous effects

(defn grand-sovereign-supreme-master-general-resolver
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

(defn apply-functions-to-objects
  [objects functions metadata]
  (grand-sovereign-supreme-master-general-resolver
    (if (contains? metadata :m-easing) metadata {:m-easing 1/16})
    (reduce (fn [acc func] (func acc)) objects functions)))

; (resolve-function-timing
; [  {:type "Sprite",
;    :filepath "sb/lyrics/48.png",
;    :metadata {:start 107053, :end 111853},
;    :tether "Centre",
;    :functions
;    [{:function "S", :start 0, :easing 0, :end 1, :arguments [0.15]}
;     {:function "F", :start 0, :end 1/8, :easing 1, :arguments [0 1]}
;     {:function "F", :start 7/8, :end 1, :easing 1, :arguments [1 0]}],
;    :layer "Foreground",
;    :position [449.63750000000005 411.475]}
;   {:type "Sprite",
;    :filepath "sb/lyrics/72.png",
;    :metadata {:start 107053, :end 111853},
;    :tether "Centre",
;    :functions
;    [{:function "S", :start 0, :easing 0, :end 1, :arguments [0.15]}
;     {:function "F", :start 0, :end 1/8, :easing 1, :arguments [0 1]}
;     {:function "F", :start 7/8, :end 1, :easing 1, :arguments [1 0]}],
;    :layer "Foreground",
;    :position [461.1875 411.475]}])

;
; (defn resolve-overlapping-effects
;   "Granddaddy function responsible for handling an interlaced effect. Think
;   _these objects are already in motion, but i want to add an additional movement
;   to this moving effect to make it more shpecial & unique_ ... its designed to
;   handle movement primarily,
;   but can deal with any interlacing effects."
;   [old functions]
;   ())
;
; ;; Sample im    (let [out (ut/grand-sovereign-supreme-master-general-resolver {:m-easing 1/32} movement-test-input)])))

; (def [{}{}{}{}]
;
; ;; Sample rotation-routine
;
; (def sample-rotation-routine
;   [{:center {:x 320 :y 240}
;     :easing 0
;     :quantization 6
;     :angle-delta 90}])
;
; (defn apply-rotation-routine-to-image-set
;   [image-set rotation-routine]
;   (let []))

;TODO GRAND RESOLVER CONCEPT

; {:easing-segmentation-count
;  :rotation-segmentation-count}

; okay so grand-resolver needs to fractionalize the movements into all submovements,
;  then fractionalize any easings and handle the position at each fraction state.
; |...|...|...|...|...|...|...|...|...|...| :easing 1
; |.........|.........|.........|.........| :easing 1
; ->
; |...|...|.|.|...|...|...|...|.|.|...|...| :easing 1 ;for ex.
;
; Movement: Use movement resolver to find position at each |.
;
; Fade: use same system, but apply multiplication to resolve fade value at |
;
; Rotate: Rotation resolver to find rotate at time.
