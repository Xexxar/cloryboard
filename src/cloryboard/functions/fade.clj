(ns cloryboard.functions.fade
  (:require [cloryboard.common.maths :as maths]
            [cloryboard.functions.common :as common]))

(defn fade
  "Applies a fade function call on all objects."
  [parameters objects]
  (common/apply-to-all-objects
    [{:function "F"
    :start (get parameters :start)
    :end (get parameters :end)
    :easing (get parameters :easing)
    :arguments [(get-in parameters [:arguments 0]) (get-in parameters [:arguments 1])]}]
    objects))

(defn fade-if-time
  "Fades a set of objects with a :restriction fn that acts on the object"
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
        (if (= (get parameters :cond-time) (get-in (filterv #(= "S" (get % :function)) (get object :functions)) [0 :end]))
        [{:start (get parameters :start)
          :function "F"
          :easing (get parameters :easing)
          :end (get parameters :end)
          :arguments (get parameters :arguments)}] []))
      objects)
      objects))

(defn fade-if-start-time
  "Fades a set of objects with a :restriction fn that acts on the object"
  [parameters objects]
  (common/apply-to-objects-sequentially
    (mapv
      (fn [object]
        (if (= (get parameters :cond-time) (get-in (filterv #(= "S" (get % :function)) (get object :functions)) [0 :start]))
        [{:start (get parameters :start)
          :function "F"
          :easing (get parameters :easing)
          :end (get parameters :end)
          :arguments (get parameters :arguments)}] []))
      objects)
      objects))

(defn fade-in-and-out
  "Fades in... and then back out."
  [parameters objects]
  (common/apply-to-all-objects
    [{:function "F"
    :start (get parameters :fade-in-start)
    :end (get parameters :fade-in-end)
    :easing (get parameters :fade-in-easing)
    :arguments [0 1]}
    {:function "F"
    :start (get parameters :fade-out-start)
    :end (get parameters :fade-out-end)
    :easing (get parameters :fade-out-easing)
    :arguments [1 0]}]
    objects))
