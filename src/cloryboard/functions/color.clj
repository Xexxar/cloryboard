(ns cloryboard.functions.color
  (:require [cloryboard.common.maths :as maths]
            [cloryboard.functions.common :as common]))

(defn color
  "Applies a color function call on all objects."
  [parameters objects]
  (common/apply-to-all-objects
    [{:function "C"
    :start (get parameters :start)
    :end (get parameters :end)
    :easing (get parameters :easing)
    :arguments [(get-in parameters [:arguments 0]) (get-in parameters [:arguments 1]) (get-in parameters [:arguments 2])
                (get-in parameters [:arguments 3]) (get-in parameters [:arguments 4]) (get-in parameters [:arguments 5])]}]
    objects))