(ns cloryboard.functions.oscilator
  (:require [cloryboard.common.maths :as maths]))

;; f-metadata
; {:number
;  :movement-delta ;; [] vector format of
;  :start :fraction
;  :end :fraction
;  :easing :number}

(defn float-up-and-down
  "Floats up and down"
  [f-metadata objects]
  (mapv
    (fn [elm]
      (assoc elm :functions
        (reduce conj (get elm :functions)
          (mapv (fn [] ())
            ))))
    objects))
