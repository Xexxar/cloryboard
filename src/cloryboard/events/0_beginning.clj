(ns cloryboard.events.0_beginning
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [cloryboard.common.effects :as effects]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.functions.fade :as fade]
            [cloryboard.functions.move :as move]
            [cloryboard.common.resolver :as resolver]))

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
    :position [320 150]
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
   {:filepath "sb/iam.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 320]
    :functions [{:function "S"
                 :start 6253
                 :end 20653
                 :easing 0
                 :arguments [0.2]}
                {:function "F"
                 :start 10453
                 :end 15853
                 :easing 17
                 :arguments [0 1]}
                {:function "F"
                 :start 15853
                 :end 20653
                 :easing 17
                 :arguments [1 0]}]}
   {:filepath "sb/cross-alone-purewhite.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 200]
    :functions [{:function "S"
                 :start 20653
                 :end 30253
                 :easing 0
                 :arguments [0.25]}
                {:function "F"
                 :start 20653
                 :end 21853
                 :easing 17
                 :arguments [0 1]}
                {:function "F"
                 :start 28453
                 :end 30253
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
  [{:effect lyrics/create-text
    :effect-parameters
    {:line "Beatmap & Storyboard\nby\nXexxar"
     :position [500 300]
     :time {:start 20653 :end 30253}
     :text-offsets {:h 10 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 3/4 :fade-out-end 1 :fade-out-easing 6})]
    :metadata {:optimizer-epsilon-base 1}}
   {:effect lyrics/create-text
    :effect-parameters
    {:line "Hitsounds\nby\nfoss"
     :position [320 380]
     :time {:start 20653 :end 30253}
     :text-offsets {:h 10 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 3/4 :fade-out-end 1 :fade-out-easing 6})]
    :metadata {}}
   {:effect lyrics/create-text
    :effect-parameters
    {:line "Alpha & Omega Sliders\nby\nOliBomby"
     :position [140 300]
     :time {:start 20653 :end 30253}
     :text-offsets {:h 10 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 3/4 :fade-out-end 1 :fade-out-easing 6})]
    :metadata {}}
   {:effect lyrics/create-text
    :effect-parameters
    {:line "For His Glory Alone"
     :position [320 100]
     :time {:start 20653 :end 30253}
     :text-offsets {:h 10 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 3/4 :fade-out-end 1 :fade-out-easing 6})]
    :metadata {}}
    ])

(defn main
  []
  [hand-commands
   (effects/create-effects lines)])
