(ns cloryboard.functions.move
  (:require [cloryboard.common.maths :as maths]
            [cloryboard.functions.common :as common]))

(defn move
  "Moves a set of objects"
  [parameters objects]
  (common/apply-to-all-objects
  [{:start (get parameters :start)
    :function "M"
    :easing (get parameters :easing)
    :end (get parameters :end)
    :arguments (if (= (count (get parameters :arguments)) 2)
                [0 0 (get-in parameters [:arguments 0]) (get-in parameters [:arguments 1])]
                [(get-in parameters [:arguments 0]) (get-in parameters [:arguments 1]) (get-in parameters [:arguments 2]) (get-in parameters [:arguments 3])])
    :metadata (if (= (count (get parameters :arguments)) 2) {:discontinous false} {:discontinous true})}]
    objects))

(defn scatter-objects-from-position
  "Moves a set of objects"
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
        ())
      objects)
    objects))

; (defn move-with-restrictions
;   "Moves a set of objects with a :restriction fn that acts on position"
;   [parameters objects]
;   (let [start (get parameters :start)
;         end (get parameters :end)
;         easing (get parameters :easing)
;         movement (get parameters :movement)]
;     (mapv
;       (fn [object]
;         (assoc object :functions
;           (conj (get object :functions)
;             {:start start
;              :function "M"
;              :easing easing
;              :end end
;              :arguments [0 0 (get movement 0) (get movement 1)]})))
;     objects)))
;
(defn move-random-percent-of-movement
  "moves an effect"
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
				(let [rng (rand 1)]
        [{:start (get parameters :start)
          :function "M"
          :easing (get parameters :easing)
          :end (get parameters :end)
          :arguments [0 0 (* rng (get-in parameters [:arguments 0])) (* rng (get-in parameters [:arguments 1]))]}]))
    	objects)
		objects))
