(ns cloryboard.functions.common
  (:require [cloryboard.common.maths :as maths]))

;; Common sub routines used by functions

(defn apply-to-all-objects
  "Applies a vector of effects to all objects."
  [effects objects]
  (mapv
    (fn [elm]
      (assoc elm :functions
        (reduce conj (get elm :functions)
          effects)))
    objects))

(defn apply-to-objects-sequentially
  "Applies a vector of effects to each object sequentially. Typically used for
  when an effect has some filtering criteria where it applies % wise based on
  some :restriction function"
  [vector-of-effects objects]
  (reduce
    (fn [acc index]
			(conj acc
      	(assoc (get objects index) :functions
        	(reduce conj (get-in objects [index :functions])
          	(get vector-of-effects index)))))
    []
    (range (count objects))))

(defn get-current-effect-value-at-time
	"Stupid function thats needed to get the current effect at a time. Needed to
	calculate effects that depend on current value."
	[object time-fraction]
	)

; (defn apply-function-specific-random-time-delay
;   "Provide a :filter-function that specifies which functions this time delay
;   applies to, and the [x y] window that applies for this time-delay."
;   [metadata objects]
;   (let [filter-function (get metadata :filter-function)
;         time-delta (get metadata :time-delta)]
;     (mapv
;       (fn [obj]
;         (let [functions (get obj :functions)
;               apply (filterv filter-function functions)
;               dont (filterv #(not (filter-function %)) functions)]
;         (assoc obj :functions
;         (reduce conj
;           dont
;           (mapv
;             #(assoc-in % [:metadata :time-delta] (rand time-delta))
;             apply)))))
;       objects)))
