(ns cloryboard.effects.spectrum
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [cloryboard.common.image :as image]
            [cloryboard.functions.common :as func-common]
            [cloryboard.common.resolver :as resolver]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(def ms-volume-data
	 (let [vols (mapv #(read-string %) (str/split (slurp "resources/volume_data_voice.edn") #"\n"))]
	 	 (reduce 
	 	   (fn [acc elm]
	 	 	   (merge acc {(+ 320 elm) (Math/pow Math/E (/ (get vols elm) 20))}))
	 	   {}
	 	   (range (count vols)))))

(defn avg-surrounding-context
  [time window]
  (/ (reduce (fn [acc elm]
  		(+ acc (get ms-volume-data (+ time elm))))
  		0
  	 (range window)) window))

(defn convert-frac-to-time
  [start end frac]
  (+ start (int (* (- end start) frac))))

(defn create-volume-effect
  [parameters]
  (let [time (get parameters :time)
  						start (get-in parameters [:time :start])
  						end (get-in parameters [:time :end])
        position (get parameters :position)
        fraction (get parameters :fraction)
        fractions (rest (mapv #(* % fraction) (range (/ 1 fraction))))
        easing (get parameters :easing)
        image (get parameters :image)
        scale (get parameters :scale)]
     [{:type "Sprite"
       :filepath image
       :metadata time
       :tether "Centre"
       :layer "Foreground"
       :position position
       :functions (reduce conj 
       													(mapv (fn [elm]
       														{:function "S"
       														 :start (- elm fraction);(convert-frac-to-time start end (- elm fraction))
       														 :end elm;(convert-frac-to-time start end elm)
       														 :easing easing
       														 :arguments [(* scale (avg-surrounding-context (convert-frac-to-time start end (- elm fraction)) 10))
       														 												(* scale (avg-surrounding-context (convert-frac-to-time start end elm) 10))]})
       															fractions)
       													 [

       														  ])}]))