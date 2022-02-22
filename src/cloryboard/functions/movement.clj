(ns cloryboard.functions.movement
  (:require [cloryboard.common.maths :as maths]))

;; f-metadata
; {:number
;  :movement-delta ;; [] vector format of
;  :start :fraction
;  :end :fraction
;  :easing :number}

(defn move
  "moves an effect"
  [f-metadata objects]
  (let [start (get f-metadata :start)
        end (get f-metadata :end)
        easing (get f-metadata :easing)
        movement (get f-metadata :movement)]
    (mapv
      (fn [object]
        (assoc object :functions
          (conj (get object :functions)
            {:start start
             :function "M"
             :easing easing
             :end end
             :arguments [0 0 (get movement 0) (get movement 1)]})))
    objects)))

(defn move-random-percent-of-movement
  "moves an effect"
  [f-metadata objects]
  (let [start (get f-metadata :start)
        end (get f-metadata :end)
        easing (get f-metadata :easing)
        movement (get f-metadata :movement)]
    (mapv
      (fn [object]
        (let [rng (rand 1)]
        (assoc object :functions
          (conj (get object :functions)
            {:start start
             :function "M"
             :easing easing
             :end end
             :arguments [0 0 (* rng (get movement 0)) (* rng (get movement 1))]}))))
    objects)))
