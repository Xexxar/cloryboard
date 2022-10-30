(ns cloryboard.events.a-3-ending
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.effects.spectrum :as spectrum]
            [cloryboard.common.effects :as effects]
            [cloryboard.functions.common :as common]
            [cloryboard.functions.move :as move]
            [cloryboard.functions.rotate :as rotate]
            [cloryboard.functions.color :as color]
            [cloryboard.functions.fade :as fade]
            [cloryboard.functions.scale :as scale]
            [cloryboard.functions.restrictions :as restrict]
            [cloryboard.effects.particles :as particles]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(def final-white
  [
   {:filepath "sb/white.jpg"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 230]
     :functions [{:function "F",
                  :start 599345,
                  :easing 7,
                  :end 601931,
                  :arguments [1 0]}
                 {:function "F",
                  :start 632705,
                  :easing 3,
                  :end 640173,
                  :arguments [0 1]}
                 {:function "S",
                  :start 599345,
                  :easing 0,
                  :end 642822,
                  :arguments [0.75 0.75]}]}])

(def hand-commands
  [{:filepath "sb/abible.png"
     :type "Sprite"
     :layer "Foreground"
     :tether "BottomCentre"
     :position [320 680]
     :functions [{:function "S",
                  :start 599345,
                  :easing 0,
                  :end 640173,
                  :arguments [0.5 0.5]}
                 {:function "M",
                  :start 599345,
                  :easing 19,
                  :end 603482,
                  :arguments [320 620 320 480]}
                 ; {:function "M",
                 ;  :start 599345,
                 ;  :easing 18,
                 ;  :end 603482,
                 ;  :arguments [320 480 320 620]}
                 ]}
   {:filepath "sb/cross-alone-purewhite.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [319 255]
    :functions [{:function "F"
    													:arguments [1 0]
    													:start 642822
    													:easing 5
    												 :end 658519}]}



                 ])

(def particles
  [{:effect particles/create-particle-routine
    :effect-parameters
    {:count 178
     :scale-range [0.25 0.75]
     :files ["sb/dot.png"]
     :time  {:start 599345 :end 632448}
     :movements [{:easing 0 :start 0 :end 5/4 :arguments [0 -7000]}]}
    :functions [(partial color/color {:start 0 :end 1 :easing 0 :arguments [210 10 10 210 10 10]})
                (partial scale/scale {:start 1 :end 5/4 :easing 0 :arguments [10]})]}])

(def particles2
  [{:effect particles/create-particle-routine
    :effect-parameters
    {:count 22
     :scale-range [0.75 1]
     :files ["sb/dot.png"]
     :time  {:start 599345 :end 632448}
     :movements [{:easing 0 :start 0 :end 5/4 :arguments [0 -21000]}]}
    :functions [(partial color/color {:start 0 :end 1 :easing 0 :arguments [210 10 10 210 10 10]})
                ; (partial color/color {:start 0 :end 1 :easing 0 :arguments [210 10 10 210 10 10]})
                (partial scale/scale {:start 1 :end 5/4 :easing 3 :arguments [10]})]}])

(def lyrics
    [{:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am your covenant - your hero\nin these blood stained pages"
     :time {:start 599345 :end 603482}}
    :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
    												(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-40 0] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 50] :start 0 :end 9/8})
                 (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ -3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial move/move-with-restriction {:restriction restrict/rand-posneg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -440] :start 0 :end 9/8})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 1 :fade-out-end 9/8 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am your guilt, your sin,\nyour debt fulfilled for all the ages"
     :time {:start 603482 :end 607620}}
    :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
    												(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-40 0] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 50] :start 0 :end 9/8})
                 (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ -3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial move/move-with-restriction {:restriction restrict/rand-posneg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -440] :start 0 :end 9/8})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 1 :fade-out-end 9/8 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I step into your shoes,\nyour substitute, your sacrifice"
     :time {:start 607620 :end 611758}}
    :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
    												(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-40 0] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 50] :start 0 :end 9/8})
                 (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ -3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial move/move-with-restriction {:restriction restrict/rand-posneg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -440] :start 0 :end 9/8})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 1 :fade-out-end 9/8 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line "your raison d'etre"
     :time {:start 611241 :end 612405}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line "your second chance"
     :time {:start 612405 :end 613439}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "your"
     :time {:start 613439 :end 613827}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "breath"
     :time {:start 613827 :end 614215}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "of"
     :time {:start 614215 :end 614603}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "life"
     :time {:start 614603 :end 615379}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "I AM"
     :time {:start 615379 :end 615896}}
      :functions [;(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am the joy of angels\ndancing in the streets of heaven"
     :time {:start 615896 :end 620034}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
    												(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-40 0] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 50] :start 0 :end 9/8})
                 (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ -3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial move/move-with-restriction {:restriction restrict/rand-posneg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -440] :start 0 :end 9/8})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 1 :fade-out-end 9/8 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am the sinner's prayer\nfor mercy and a past forgiven"
     :time {:start 620034 :end 624172}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
    												(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-40 0] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 50] :start 0 :end 9/8})
                 (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ -3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial move/move-with-restriction {:restriction restrict/rand-posneg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -440] :start 0 :end 9/8})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 1 :fade-out-end 9/8 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "I am the lamb upon\nthe altar dying willingly"
     :time {:start 624172 :end 628310}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
    												(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-40 0] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 50] :start 0 :end 9/8})
                 (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ -3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial move/move-with-restriction {:restriction restrict/rand-posneg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -440] :start 0 :end 9/8})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 1 :fade-out-end 9/8 :fade-out-easing 0})]}   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line "All hope that was,"
     :time {:start 628051 :end 629086}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line "all hope that is,"
     :time {:start 629086 :end 629991}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "all"
     :time {:start 629991 :end 630379}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "hope"
     :time {:start 630379 :end 630767}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "to"
     :time {:start 630767 :end 631155}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
   {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.3
     :line "be"
     :time {:start 631155 :end 631931}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 1 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :line "I AM"
     :scale 0.3
     :time {:start 631931 :end 642812}}
      :functions [(partial fade/fade {:start 0 :end 1/24 :easing 0 :arguments [0 1]})
                  (partial color/color {:start 2/25 :end 4/5 :easing 3 :arguments [255 255 255 0 0 0]})
(partial move/move {:easing 17 :arguments [0 -60] :start 0 :end 7/8})]}
])

(defn main
  []
  [(effects/create-effects particles)
   hand-commands
   (effects/create-effects particles2)
   final-white
   (effects/create-effects lyrics)]) 