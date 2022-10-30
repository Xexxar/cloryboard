(ns cloryboard.functions.scale
  (:require [cloryboard.common.maths :as maths]
            [cloryboard.functions.common :as common]
            [cloryboard.functions.move :as move]))

(defn scale
  "Applies a scales on each subobject."
  [parameters objects]
  (common/apply-to-objects-sequentially
  (mapv (fn [object]
    [{:function "S"
    :start (get parameters :start)
    :end (get parameters :end)
    :easing (get parameters :easing)
    :arguments [(get-in (filterv #(= "S" (get % :function)) (get object :functions)) [0 :arguments 0]) (* (get-in (filterv #(= "S" (get % :function)) (get object :functions)) [0 :arguments 0])
    																																		(get-in parameters [:arguments 0]))]}])
    objects)
    objects))

(defn scale-effect-from-position
  "Scales the effect from the provided :position relative to arguments. This
	assumes a :argument value."
  [parameters objects]
	(move/scatter-objects-from-position
		parameters
  	(common/apply-to-all-objects
    	[{:function "S"
    	  :start (get parameters :start)
    		:end (get parameters :end)
    		:easing (get parameters :easing)
    		:arguments [1 (get parameters :argument)]}]
    	objects)))
