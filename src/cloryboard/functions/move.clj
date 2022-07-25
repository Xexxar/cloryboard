(ns cloryboard.functions.move
  (:require [cloryboard.common.maths :as maths]
            [cloryboard.functions.common :as common]
            [cloryboard.common.resolver :as resolver]))

(defn move
  "Moves a set of objects"
  [parameters objects]
  (common/apply-to-all-objects
  [{:start (get parameters :start)
    :function "M"
    :easing (get parameters :easing)
    :end (get parameters :end)
    :arguments (get parameters :arguments)}]
    objects))

(defn scatter-objects-from-position
  "Moves a set of objects away from a target :position"
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
        ())
      objects)
    objects))

(defn move-with-restriction
  "Moves a set of objects with a :restriction fn that acts on the object"
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
        [{:start (get parameters :start)
          :function "M"
          :easing (get parameters :easing)
          :end (get parameters :end)
          :arguments ((get parameters :restriction) object parameters)}])
      objects)
      objects))

(defn move-out-from-position
  "Moves object greater the farther it is from the :position."
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
				(let [currpos (resolver/get-current-effect-value object (get parameters :start) "M")
              ratio (/ (maths/vec-distance (maths/vec-subtract currpos (get parameters :position))) 100)]
        [{:start (get parameters :start)
          :function "M"
          :easing (get parameters :easing)
          :end (get parameters :end)
          :arguments (maths/vec-multiply ratio (get parameters :arguments))}]))
    	objects)
		objects))

(defn move-random-percent-of-movement
  "moves an effect a random amount "
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
				(let [rng (rand 1)]
        [{:start (get parameters :start)
          :function "M"
          :easing (get parameters :easing)
          :end (get parameters :end)
          :arguments [(* rng (get-in parameters [:arguments 0])) (* rng (get-in parameters [:arguments 1]))]}]))
    	objects)
		objects))

(defn move-continuously
  "Moves all objects in a direction and when any object goes offscreen it
  restarts from the opposite edge."
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
				(let [rng (rand 1)]
        [{:start (get parameters :start)
          :function "M"
          :easing (get parameters :easing)
          :end (get parameters :end)
          :arguments [(* rng (get-in parameters [:arguments 0])) (* rng (get-in parameters [:arguments 1]))]}]))
    	objects)
		objects))
