(ns cloryboard.events.2_verse1
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
            [clojure.java.io :as io]
            [cloryboard.effects.particles :as particles])
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
  	    :fraction 1/640
  	    :time {:start 107053 :end 155053}}}])

(def lyrics
  [{:effect lyrics/create-text
		:effect-parameters
    {:line "I am the light upon your path\nwhen you have lost your way"
     :position [320 400]
     :time {:start 107053 :end 111853}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})]
		:metadata {:m-easing 1/32}}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the footprints in the sand\nthe ocean's tide can't wash away"
     :position [320 400]
     :time {:start 111853 :end 116653}
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
    {:line "I am the shelter from the storm\nthat rages on and on"
     :position [320 400]
     :time {:start 116653 :end 121453}
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
    {:line "The incorruptible foundation \nthat the wise man builds upon"
     :position [320 400]
     :time {:start 121453 :end 126253}
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
    {:line "I am the bread that feeds a\nstarving man upon the street"
     :position [320 400]
     :time {:start 126253 :end 131053}
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
    {:line "I am the bounty on the table\nin the palace at the feast"
     :position [320 400]
     :time {:start 131053 :end 135853}
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
    {:line "I am the rain upon the earth\nafter a scorching drought"
     :position [320 400]
     :time {:start 135853 :end 140653}
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
    {:line "I am the quenching of the thirst\nyou never thought you'd be without"
     :position [320 400]
     :time {:start 140653 :end 145453}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 1 :end 15/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 1 :end 15/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 1 :end 15/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 12/8 :fade-out-end 15/8 :fade-out-easing 1})]}])

;
(defn main
  []
  [(effects/create-effects spectrum)
   (effects/create-effects lyrics)	]
)
