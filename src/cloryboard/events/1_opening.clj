(ns cloryboard.events.1_opening
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(def hand-commands
  [{:filepath "sb/background.png"
     :type "Sprite"
     :layer "Background"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "S",
                  :start 30253,
                  :easing 0,
                  :end 640173,
                  :arguments [0.6]}]}
  {:filepath "sb/alphabw/lightning1.png"
   :type "Sprite"
   :layer "Background"
   :tether "Centre"
   :position [-30 280]
   :functions [{:function "S",
                :start 51553,
                :easing 0,
                :end 53053,
                :arguments [1.25]}
               {:function "F",
                :start 51553,
                :easing 0,
                :end 53053,
                :arguments [1 0]}]}
{:filepath "sb/glow.png"
 :type "Sprite"
 :layer "Background"
 :tether "Centre"
 :position [84 222]
 :functions [{:function "R",
              :start 51553,
              :easing 0,
              :end 53053,
              :arguments [-0.261799, -0.261799]}
             {:function "S",
              :start 51553,
              :easing 0,
              :end 53053,
              :arguments [50.0, 50]}
             {:function "F",
              :start 51553,
              :easing 7,
              :end 53053,
              :arguments [1 0]}]}])



(defn main
  []
  [hand-commands])
