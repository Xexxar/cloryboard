(ns cloryboard.effects.common
  (:require [cloryboard.common.maths :as maths]))

;; Common functions used when generating an effect.

;;NOTE [objects arguments1 arguments2] for effects
;;NOTE [metadata objects] for functions

(defn position-effect
  "Positions an effect to start at a specific place."
  [objects tether position]
  (let [tether-pos (maths/get-tether-position objects tether)
        offset (maths/vec-subtract position tether-pos)]
  (mapv
    #(-> %
      (update-in [:position 0] (fn [x] (+ x (get offset 0))))
      (update-in [:position 1] (fn [x] (+ x (get offset 1)))))
    objects)))

(defn scale-effect
  "Sets the initial position and applies a scale function based on some "
  [objects scale]
  (let [center (maths/get-center-position objects)]
    (mapv
      #(let [new-position (maths/vec-add center (maths/vec-multiply scale (maths/vec-subtract (get % :position) center)))]
        (-> %
        (assoc-in [:functions (count (get % :functions))]
                  {:function "S"
                   :start 0/1
                   :easing 0
                   :end 1/1
                   :arguments [scale]})
        (update-in [:position] (fn [x] new-position))))
    objects)))
