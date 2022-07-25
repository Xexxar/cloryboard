(ns cloryboard.functions.rotate
  (:require [cloryboard.common.maths :as maths]
            [cloryboard.functions.common :as common]))

(defn rotate
  "Rotate a set of objects"
  [parameters objects]
  (common/apply-to-all-objects
  [{:start (get parameters :start)
    :function "R"
    :easing (get parameters :easing)
    :end (get parameters :end)
    :arguments (if (= (count (get parameters :arguments)) 1)
                [0 (get-in parameters [:arguments 0])]
                [(get-in parameters [:arguments 0]) (get-in parameters [:arguments 1])])
    :metadata (if (= (count (get parameters :arguments)) 2) {:discontinous true} {:discontinous false})}]
    objects))

(defn rotate-effect
  "Applies a rotation to each object around a point."
  []
  )


(defn rotate-with-restriction
  "Rotates a set of objects with a :restriction fn that acts on the object"
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
        [{:start (get parameters :start)
          :function "R"
          :easing (get parameters :easing)
          :end (get parameters :end)
          :arguments ((get parameters :restriction) object parameters)}])
      objects)
      objects))
