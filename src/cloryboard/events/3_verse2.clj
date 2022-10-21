(ns cloryboard.events.3_verse2
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.common.effects :as effects]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.effects.spectrum :as spectrum]
            [cloryboard.functions.fade :as fade]
            [cloryboard.functions.move :as move]
            [cloryboard.functions.restrictions :as restrict]
            [cloryboard.functions.rotate :as rotate]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(def spectrum
  [{:effect spectrum/create-volume-effect
  	 :effect-parameters 
  	   {:image "sb/circle.png"
  	    :scale 2
  	    :easing 0
  	    :position [320 170]
  	    :fraction 1/512
  	    :time {:start 155053  :end 193381}}}])

(def lyrics
  [{:effect lyrics/create-text
		:effect-parameters
    {:line "I am the melody that weaves\nit's way inside your soul"
     :position [320 400]
     :time {:start 155053 :end 159853}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]
		:metadata {:m-easing 1/32}}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the symphony, the masterpiece,\nthe actor's greatest role"
     :position [320 400]
     :time {:start 159853 :end 164653}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]
		:metadata {}}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the poetry that speaks\nto you with every rhyme"
     :position [320 400]
     :time {:start 164653 :end 169453}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})]}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the songwriter who seems\nto write your life in every line"
     :position [320 400]
     :time {:start 169453 :end 174253}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]
		:metadata {}}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the simple truth that\nshapes your world from your birth"
     :position [320 400]
     :time {:start 174253 :end 179053}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the vast volumes of knowledge\nspanning all across the earth"
     :position [320 400]
     :time {:start 179053 :end 183853}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the whisper of the wind\nyou feel but cannot see"
     :position [320 400]
     :time {:start 183853 :end 188653}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}])

;
(defn main
  []
  [(effects/create-effects spectrum)
   (effects/create-effects lyrics)]
)
