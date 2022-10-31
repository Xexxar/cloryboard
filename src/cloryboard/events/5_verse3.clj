(ns cloryboard.events.5-verse3
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
  	   	; :image "sb/dot.png"
  	    ; :scale 40
  	    :easing 0
  	    :position [320 170]
  	    :fraction 1/512
  	    :time {:start 240292 :end 278625}}
  	 :functions []}])

(def flash
  [{:filepath "sb/white.jpg"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "F",
                  :start 259492,
                  :easing 7,
                  :end 260392,
                  :arguments [0.75 0]}
                 {:function "F",
                  :start 260392,
                  :easing 7,
                  :end 261892,
                  :arguments [0.75 0]}
                 {:function "F",
                  :start 261892,
                  :easing 7,
                  :end 262792,
                  :arguments [0.75 0]}
                 {:function "F",
                  :start 262792,
                  :easing 7,
                  :end 264292,
                  :arguments [0.75 0]}
                 {:function "P"
                  :start 259492
                  :easing 0
                  :end 264292
                  :arguments "A"}]}])

(def lyrics
  [{:effect lyrics/create-text
		:effect-parameters
    {:line "I am the trusting child\nwhose simple faith is ever sure"
     :position [320 400]
     :time {:start 240292 :end 245092}
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
    {:line "I am the parent's love,\nunchanging, unconditional and pure"
     :position [320 400]
     :time {:start 245092 :end 249892}
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
    {:line "I am the loyal friend whose\nheart will never let you down"
     :position [320 400]
     :time {:start 249892 :end 254692}
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
    {:line "I am the hand that pulls you\nback into the boat before you drown"
     :position [320 400]
     :time {:start 254692 :end 259492}
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
    {:line "I am the thunder and the\nglory and the blinding light"
     :position [320 400]
     :time {:start 259492 :end 264292}
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
    {:line "I am the still small voice that tells\nyou what is wrong and what is right"
     :position [320 400]
     :time {:start 264292 :end 269092}
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
    {:line "I am the sacrificial lamb\na guilty world reviled"
     :position [320 400]
     :time {:start 269092 :end 273892}
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
   (effects/create-effects lyrics)
   flash]
)
