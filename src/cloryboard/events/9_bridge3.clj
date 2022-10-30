(ns cloryboard.events.9-bridge3
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.common.effects :as effects]
            [cloryboard.functions.common :as common]
            [cloryboard.functions.move :as move]
            [cloryboard.functions.rotate :as rotate]
            [cloryboard.functions.fade :as fade]
            [cloryboard.functions.restrictions :as restrict]
            [cloryboard.effects.particles :as particles]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(def hand-commands
	[{:filepath "sb/white.jpg"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "F",
                  :start 465090,
                  :easing 7,
                  :end 465972,
                  :arguments [1 0]}]}
   ; {:filepath "sb/white.jpg"
   ;   :type "Sprite"
   ;   :layer "Foreground"
   ;   :tether "Centre"
   ;   :position [320 240]
   ;   :functions [{:function "F",
   ;                :start 466854,
   ;                :easing 7,
   ;                :end 467736,
   ;                :arguments [1 0]}]}
   {:filepath "sb/white.jpg"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "F",
                  :start 469501,
                  :easing 7,
                  :end 470383,
                  :arguments [1 0]}]}
   ])


(def lyrics
	[{:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 256]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :line "There is a way which seemeth right unto a man,\nBut the end thereof are the ways of death.\n\nProverbs 14:12"
     :time {:start 439738 :end 447010}}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 1})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 256]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :line "The fool hath said in his heart, There is no God.\nThey are corrupt, they have done abominable works,\nthere is none that doeth good.\n\nPsalm 14:1"
     :time {:start 447010 :end 454283}}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 1})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 256]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :line "To me belongeth vengeance, and recompence;\ntheir foot shall slide in due time: for the\nday of their calamity is at hand, and the\nthings that shall come upon them make haste.\n\nDeuteronomy 32:35"
     :time {:start 454283 :end 462002}}
    :functions [(partial fade/fade {:start 0 :end 1/8 :easing 1 :arguments [0 1]})]}
    ])

(def particles
  [{:effect particles/create-particle-routine
    :effect-parameters
    {:count 200
     :scale-range [0.25 1]
     :files ["sb/dot.png"]
     :time  {:start 462884 :end 465090}
     :movements [
                 {:easing 10 :start 0 :end 1 :arguments [0 -10]}]}
    :functions [(partial fade/fade {:start 0 :end 3/5 :easing 1 :arguments [0 1]})
                ]}
{:effect particles/create-particle-routine
    :effect-parameters
    {:count 200
     :scale-range [0.25 1]
     :files ["sb/dot.png"]
     :time  {:start 465090 :end 466854}
     :movements [{:easing 0 :start 0 :end 1 :arguments [0 350]}]}
    :functions []}
{:effect particles/create-particle-routine
    :effect-parameters
    {:count 200
     :scale-range [0.25 1]
     :files ["sb/dot.png"]
     :time  {:start 467295 :end 469501}
     :movements [
                 {:easing 10 :start 0 :end 1 :arguments [0 -10]}]}
    :functions [(partial fade/fade {:start 0 :end 3/5 :easing 1 :arguments [0 1]})
                ]}
{:effect particles/create-particle-routine
    :effect-parameters
    {:count 200
     :scale-range [0.25 1]
     :files ["sb/dot.png"]
     :time  {:start 469501 :end 473030}
     :movements [{:easing 0 :start 0 :end 7/8 :arguments [0 700]}
                 {:easing 4 :start 7/8 :end 12/8 :arguments [0 250]}]}
    :functions []}


    ])

(defn main
  []
  [hand-commands
   (effects/create-effects particles)
   (effects/create-effects lyrics)]) 