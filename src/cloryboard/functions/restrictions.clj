(ns cloryboard.functions.restrictions
  (:require [cloryboard.common.maths :as maths]
            [cloryboard.functions.common :as common]
            [cloryboard.common.resolver :as resolver]))

;; restrictions are used for

(defn ratio-based-on-dist-from-center-line
  [object parameters]
  (let [currpos (resolver/get-current-effect-value object (get parameters :start) "M")
        ratio (/ (- 320 (get currpos 0)) 100)]
    (maths/vec-multiply ratio (get parameters :arguments))))

(defn ratio-based-on-dist-from-center-line-non-neg
  [object parameters]
  (let [currpos (resolver/get-current-effect-value object (get parameters :start) "M")
        ratio (Math/abs (/ (- 320 (get currpos 0)) 100))]
    (maths/vec-multiply ratio (get parameters :arguments))))

(defn rand-pos
  [object parameters]
  (let [rng (rand 1)]
    (maths/vec-multiply rng (get parameters :arguments))))

(defn rand-posneg
  [object parameters]
  (let [rng (dec (rand 2))]
    (maths/vec-multiply rng (get parameters :arguments))))
