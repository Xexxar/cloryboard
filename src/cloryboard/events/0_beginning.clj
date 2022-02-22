(ns cloryboard.events.0_beginning
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [cloryboard.functions.common :as func-common]
            [cloryboard.functions.movement :as func-movement]
            [cloryboard.common.resolver :as resolver]
            [cloryboard.effects.lyrics :as lyrics])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(def hand-commands
  [{:filepath "bg.jpg"
     :type "Sprite"
     :layer "Background"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "F",
                  :start 0,
                  :easing 0,
                  :end 0,
                  :arguments [0]}]}
   {:filepath "sb/theocracy.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 200]
    :functions [{:function "S"
                 :start 6253
                 :end 20653
                 :easing 0
                 :arguments [0.5]}
                {:function "F"
                 :start 6253
                 :end 10453
                 :easing 17
                 :arguments [0 1]}
                {:function "F"
                 :start 15853
                 :end 20653
                 :easing 17
                 :arguments [1 0]}]}
   {:filepath "sb/glow.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 240]
    :functions [{:function "S"
                 :start 27853
                 :end 30253
                 :easing 0
                 :arguments [75]}
                {:function "R"
                 :start 27853
                 :end 30253
                 :easing 0
                 :arguments [1.5707963267948966]}
                {:function "F"
                 :start 27853
                 :end 30253
                 :easing 6
                 :arguments [0 1]}]}])

(def lines
  {:iam
    {:line "I AM"
     :position [320 300]
     :time {:start 11053 :end 20653}
     :text-offsets {:h 50 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 1/2 :fade-out-end 1 :fade-out-easing 1})]}
   :xexxar-1
    {:line "Beatmap & Storyboard\nby\nXexxar"
     :position [500 300]
     :time {:start 20653 :end 30253}
     :text-offsets {:h 10 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 3/4 :fade-out-end 1 :fade-out-easing 6})]}
   ; :xexxar-2
   ;  {:line "he who must decrease"}
   :foss
    {:line "Hitsounds\nby\nfoss"
     :position [320 380]
     :time {:start 20653 :end 30253}
     :text-offsets {:h 10 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 3/4 :fade-out-end 1 :fade-out-easing 6})]}
   :oli
    {:line "Alpha & Omega Sliders\nby\nOliBomby"
     :position [140 300]
     :time {:start 20653 :end 30253}
     :text-offsets {:h 10 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 3/4 :fade-out-end 1 :fade-out-easing 6})]}
   :god
    {:line "For His Glory Alone"
     :position [320 100]
     :time {:start 20653 :end 30253}
     :text-offsets {:h 10 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 3/4 :fade-out-end 1 :fade-out-easing 6})]}})

(defn create-lyrics
  "Generates lyrics partial to the lyric-metadata, skipping manual coding."
  [line-metadata]
  (reduce (fn [acc elm]
    (into acc (lyrics/create-lyrics (get elm 1))))
      [] line-metadata))

(defn main
  []
  [hand-commands
   (create-lyrics lines)])
