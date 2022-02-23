(ns cloryboard.functions.move
  (:require [cloryboard.common.maths :as maths]))

;; f-metadata
; {:number
;  :movement-delta ;; [] vector format of
;  :start :fraction
;  :end :fraction
;  :easing :number}

(defn move
  "Moves a set of objects"
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

(defn move-with-
  "Moves a set of objects with a :restriction fn that acts on position"
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

; (defn apply-looping-move)

;; needs to consider current vector.
