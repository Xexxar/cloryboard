(ns cloryboard.functions.common
  (:require [cloryboard.common.maths :as maths]))

(defn scale-effect
  "Staticly scales any effect."
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

(defn position-effect
  [objects tether position]
  (let [tether-pos (maths/get-tether-position objects tether)
        offset (maths/vec-subtract position tether-pos)]
  (mapv
    #(-> %
      (update-in [:position 0] (fn [x] (+ x (get offset 0))))
      (update-in [:position 1] (fn [x] (+ x (get offset 1)))))
    objects)))

(defn rotate-effect
  "Staticly rotates any effect."
  [objects]
  )

(defn fade-in-and-out
  "Fades in... and it fades out."
  [f-metadata objects]
  (mapv
    (fn [elm]
      (assoc elm :functions
        (reduce conj (get elm :functions)
          [{:function "F"
            :start (get f-metadata :fade-in-start)
            :end (get f-metadata :fade-in-end)
            :easing (get f-metadata :fade-in-easing)
            :arguments [0 1]}
           {:function "F"
            :start (get f-metadata :fade-out-start)
            :end (get f-metadata :fade-out-end)
            :easing (get f-metadata :fade-out-easing)
            :arguments [1 0]}])))
    objects))

(defn apply-function-specific-random-time-delay
  "Provide a :filter-function that specifies which functions this time delay
  applies to, and the [x y] window that applies for this time-delay."
  [metadata objects]
  (let [filter-function (get metadata :filter-function)
        time-delta (get metadata :time-delta)]
    (mapv
      (fn [obj]
        (let [functions (get obj :functions)
              apply (filterv filter-function functions)
              dont (filterv #(not (filter-function %)) functions)]
        (assoc obj :functions
        (reduce conj
          dont
          (mapv
            #(assoc-in % [:metadata :time-delta] (rand time-delta))
            apply)))))
      objects)))
