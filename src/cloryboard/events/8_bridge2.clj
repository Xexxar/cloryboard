(ns cloryboard.events.8-bridge2
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
	[{:filepath "sb/iamtri.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 220]
    :functions [{:function "S"
    													:arguments [2 0.25]
    													:start 398359
    													:easing 8
    												 :end 400014}
    												{:function "S"
    													:arguments [0.25]
    													:start 400014
    													:easing 1
    												 :end 439738}]}
  {:filepath "sb/i.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 220]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 426083
    													:easing 1
    												 :end 439738}]}
  {:filepath "sb/am.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 275]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 426497
    													:easing 1
    												 :end 439738}]}])

(def lyrics
	[{:effect lyrics/create-text
  	 :effect-parameters
    {:position [510 176]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am\npower"
     :time {:start 400014 :end 406634}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [130 176]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am\nglory"
     :time {:start 401669 :end 406634}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 400]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am love's\nunending story"
     :time {:start 403324 :end 406634}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [510 176]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am\njustice"
     :time {:start 406634 :end 413255}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [130 176]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am\nhonor"
     :time {:start 408290 :end 413255}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 400]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am hope\nand living water"
     :time {:start 409945 :end 413255}}
    :functions []}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [510 176]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am\nrighteous"
     :time {:start 413255 :end 419876}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [130 176]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am\nholy"
     :time {:start 414910 :end 419876}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 400]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am three\nand one and only"
     :time {:start 416566 :end 419876}}
    :functions []}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [510 196]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am\nsovereign"
     :time {:start 419876 :end 426497}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [130 196]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am\nfaithful"
     :time {:start 421531 :end 426497}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 400]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "Everlasting"
     :time {:start 423186 :end 426497}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 210]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "I"
     :time {:start 424841 :end 426083}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 250]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "am"
     :time {:start 425255 :end 426083}}
    :functions []}
  {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 290]
     :text-offsets {:h 15 :v 25}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "that"
     :time {:start 425669 :end 426083}}
    :functions []}
   ])

(defn main
  []
  [hand-commands
   (effects/create-effects lyrics)])
