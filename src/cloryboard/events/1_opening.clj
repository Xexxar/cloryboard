(ns cloryboard.events.1_opening
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.common.effects :as effects]
            [cloryboard.effects.lyrics :as lyrics]
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

(def hand-commands
  [
  {:filepath "sb/background.png"
     :type "Sprite"
     :layer "Background"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "S",
                  :start 30253,
                  :easing 0,
                  :end 640173,
                  :arguments [0.6]}]}
  {:filepath "sb/lightning1.png"
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
              :arguments [1 0]}]}
  {:filepath "sb/lightning4.png"
   :type "Sprite"
   :layer "Background"
   :tether "Centre"
   :position [460 280]
   :functions [{:function "S",
                :start 56353,
                :easing 0,
                :end 57853,
                :arguments [1.25]}
               {:function "F",
                :start 56353,
                :easing 0,
                :end 57853,
                :arguments [1 0]}]}
{:filepath "sb/glow.png"
 :type "Sprite"
 :layer "Background"
 :tether "Centre"
 :position [490 222]
 :functions [{:function "R",
              :start 56353,
              :easing 0,
              :end 57853,
              :arguments [0.261799, 0.261799]}
             {:function "S",
              :start 56353,
              :easing 0,
              :end 57853,
              :arguments [50.0, 50]}
             {:function "F",
              :start 56353,
              :easing 7,
              :end 57853,
              :arguments [1 0]}]}
  {:filepath "sb/rol.png"
   :type "Sprite"
   :layer "Background"
   :tether "Centre"
   :position [530 260]
   :functions [{:function "R",
                :start 61153,
                :easing 0,
                :end 62653,
                :arguments [3.34 3.34]}
               {:function "S",
                :start 61153,
                :easing 0,
                :end 62653,
                :arguments [1.25]}
               {:function "F",
                :start 61153,
                :easing 0,
                :end 62653,
                :arguments [1 0]}]}
{:filepath "sb/glow.png"
 :type "Sprite"
 :layer "Background"
 :tether "Centre"
 :position [490 222]
 :functions [{:function "R",
              :start 61153,
              :easing 0,
              :end 62653,
              :arguments [-0.261799, -0.261799]}
             {:function "S",
              :start 61153,
              :easing 0,
              :end 62653,
              :arguments [50.0, 50]}
             {:function "F",
              :start 61153,
              :easing 7,
              :end 62653,
              :arguments [1 0]}]}
  {:filepath "sb/rol3.png"
   :type "Sprite"
   :layer "Background"
   :tether "Centre"
   :position [120 260]
   :functions [{:function "R",
                :start 65953,
                :easing 0,
                :end 67453,
                :arguments [3.04 3.04]}
               {:function "S",
                :start 65953,
                :easing 0,
                :end 67453,
                :arguments [1.25]}
               {:function "F",
                :start 65953,
                :easing 0,
                :end 67453,
                :arguments [1 0]}]}
{:filepath "sb/glow.png"
 :type "Sprite"
 :layer "Background"
 :tether "Centre"
 :position [84 222]
 :functions [{:function "R",
              :start 65953,
              :easing 0,
              :end 67453,
              :arguments [0.261799, 0.261799]}
             {:function "S",
              :start 65953,
              :easing 0,
              :end 67453,
              :arguments [50.0, 50]}
             {:function "F",
              :start 65953,
              :easing 7,
              :end 67453,
              :arguments [1 0]}]}])

(def particles
  [{:effect particles/create-box-of-particles
    :effect-parameters
    {:count 200
     :scale-range [0.25 0.25]
     :files ["sb/dot.png"]
     :time  {:start 30253 :end 35053}
     :coords [[-107 0] [747 480]]}
    :functions (reduce into (mapv (fn [k] [(partial move/move {:easing 5 :arguments [0 -10] :start (* k 6/4) :end (+ 3/4 (* k 6/4))})
                      (partial move/move {:easing 5 :arguments [0 10] :start (+ 3/4 (* k 6/4)) :end (+ 6/4 (* k 6/4))})])
                (range 100)))}])


(defn main
  []
  [hand-commands
   (effects/create-effects particles)])
