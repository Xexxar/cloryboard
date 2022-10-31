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
  [{:filepath "sb/background.png"
     :type "Sprite"
     :layer "Background"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "S",
                  :start 30253,
                  :easing 0,
                  :end 320566,
                  :arguments [0.6]}
                 {:function "F",
                  :start 315906,
                  :easing 17,
                  :end 320566,
                  :arguments [1, 0]}]}   
    {:filepath "sb/white.jpg"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "F",
                  :start 47053,
                  :easing 7,
                  :end 47503,
                  :arguments [0.25 0]}
                 {:function "F",
                  :start 47503,
                  :easing 7,
                  :end 47953,
                  :arguments [0.25 0]}
                 {:function "F",
                  :start 47953,
                  :easing 7,
                  :end 48403,
                  :arguments [0.25 0]}
                 {:function "F",
                  :start 48403,
                  :easing 6,
                  :end 49453,
                  :arguments [0 0.5]}
                 {:function "F",
                  :start 49453,
                  :easing 7,
                  :end 51553,
                  :arguments [0.5 0]}
                 {:function "P"
                  :start 47053
                  :easing 0
                  :end 51553
                  :arguments "A"}]}
  {:filepath "sb/white.jpg"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "F",
                  :start 30253,
                  :easing 7,
                  :end 32653,
                  :arguments [1 0]}
                 {:function "P"
                  :start 30253
                  :easing 0
                  :end 32653
                  :arguments "A"}]}
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
   :position [540 320]
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
  [{:effect particles/create-particle-routine
    :effect-parameters
    {:count 200
     :scale-range [0.25 1]
     :files ["sb/dot.png"]
     :time  {:start 30253 :end 107053}
     :movements [{:easing 5 :start 0 :end 1/16 :arguments [0 10]}
                 {:easing 5 :start 1/16 :end 2/16 :arguments [0 -10]}
                 {:easing 5 :start 2/16 :end 3/16 :arguments [0 10]}
                 {:easing 9 :start 3/16 :end 4/16 :arguments [0 -125]}
                 {:easing 0 :start 4/16 :end 11/16 :arguments [0 -3500]}
                 {:easing 4 :start 11/16 :end 12/16 :arguments [0 -250]}
                 {:easing 3 :start 12/16 :end 13/16 :arguments [0 50]}
                 {:easing 0 :start 13/16 :end 32/16 :arguments [0 1900]}
                 {:easing 4 :start 32/16 :end 33/16 :arguments [0 50]}
                 {:easing 3 :start 33/16 :end 35/16 :arguments [0 -500]}
                 {:easing 0 :start 35/16 :end 42/16 :arguments [0 -3500]}
                 {:easing 0 :start 42/16 :end (- 43/16 1/64)  :arguments [0 -375]}
                 {:easing 4 :start (- 43/16 1/64) :end (- 44/16 1/64)  :arguments [0 -250]}
                 {:easing 3 :start (- 44/16 1/64) :end (- 45/16 1/64)  :arguments [0 50]}
                 {:easing 0 :start (- 45/16 1/64) :end (- 50/16 1/64)  :arguments [0 500]}
                 {:easing 4 :start (- 50/16 1/64) :end (- 51/16 1/64)  :arguments [0 50]}
                 {:easing 3 :start (- 51/16 1/64) :end (- 53/16 1/64)  :arguments [0 -500]}
                 {:easing 0 :start (- 53/16 1/64) :end (- 60/16 1/64)  :arguments [0 -3500]}
                 {:easing 4 :start (- 60/16 1/64) :end (- 61/16 1/64)  :arguments [0 -250]}
                 ]}
    :functions [(partial fade/fade-if-time {:arguments [1 0] :cond-time (- 61/16 1/64);(- 61/16 1/64) 
    	:start (- 60/16 1/32) :end (- 61/16 1/32) :easing 17})]}])


(defn main
  []
  [
   (effects/create-effects particles)
   hand-commands])
