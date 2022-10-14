(ns cloryboard.events.6-chorus2
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.common.effects :as effects]
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
  [{:filepath "sb/alphabw/lightning1.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "Centre",
  :position [-30 280],
  :functions
  [{:function "S",
    :start 313576,
    :easing 0,
    :end 315046,
    :arguments [1.25]}
   {:function "R",
    :start 314017,
    :easing 0,
    :end 315046,
    :arguments [0.261799 0.261799]}
   {:function "F",
    :start 313576,
    :easing 0,
    :end 315046,
    :arguments [1 0]}]}
 {:filepath "sb/alphabw/lightning4.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "Centre",
  :position [460 280],
  :functions
  [{:function "S",
    :start 314017,
    :easing 0,
    :end 315482,
    :arguments [1.25]}
   {:function "R",
    :start 314017,
    :easing 0,
    :end 315482,
    :arguments [-0.261799 -0.261799]}
   {:function "F",
    :start 314017,
    :easing 0,
    :end 315482,
    :arguments [1 0]}]}
 {:filepath "sb/rol.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "Centre",
  :position [530 260],
  :functions
  [{:function "R",
    :start 294935,
    :easing 0,
    :end 296391,
    :arguments [3.64 3.64]}
   {:function "S",
    :start 294935,
    :easing 0,
    :end 296391,
    :arguments [1.25]}
   {:function "F",
    :start 294935,
    :easing 0,
    :end 296391,
    :arguments [1 0]}]}
 {:filepath "sb/rol3.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "Centre",
  :position [120 260],
  :functions
  [{:function "R",
    :start 295372,
    :easing 0,
    :end 296828,
    :arguments [2.74 2.74]}
   {:function "S",
    :start 295372,
    :easing 0,
    :end 296828,
    :arguments [1.25]}
   {:function "F",
    :start 295372,
    :easing 0,
    :end 296828,
    :arguments [1 0]}]}
 {:filepath "sb/bible.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "BottomCentre",
  :position [320 680],
  :functions
  [{:function "S",
    :start 278625,
    :easing 0,
    :end 314752,
    :arguments [0.5 0.5]}
   {:function "M",
    :start 278625,
    :easing 19,
    :end 283285,
    :arguments [320 620 320 480]}
   {:function "M",
    :start 314752,
    :easing 18,
    :end 318036,
    :arguments [320 480 320 620]}]}
 {:filepath "sb/glow.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "Centre",
  :position [84 222],
  :functions
  [{:function "R",
    :start 313576,
    :easing 0,
    :end 314458,
    :arguments [-0.261799 -0.261799]}
   {:function "S",
    :start 313576,
    :easing 0,
    :end 314458,
    :arguments [50.0 50]}
   {:function "F",
    :start 313576,
    :easing 7,
    :end 314458,
    :arguments [1 0]}]}
 {:filepath "sb/glow.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "Centre",
  :position [490 222],
  :functions
  [{:function "R",
    :start 314017,
    :easing 0,
    :end 315482,
    :arguments [0.261799 0.261799]}
   {:function "S",
    :start 314017,
    :easing 0,
    :end 315482,
    :arguments [50.0 50]}
   {:function "F",
    :start 314017,
    :easing 7,
    :end 315482,
    :arguments [1 0]}]}
 {:filepath "sb/glow.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "Centre",
  :position [490 222],
  :functions
  [{:function "R",
    :start 294935,
    :easing 0,
    :end 296391,
    :arguments [-0.261799 -0.261799]}
   {:function "S",
    :start 294935,
    :easing 0,
    :end 296391,
    :arguments [50.0 50]}
   {:function "F",
    :start 294935,
    :easing 7,
    :end 296391,
    :arguments [1 0]}]}
 {:filepath "sb/glow.png",
  :type "Sprite",
  :layer "Foreground",
  :tether "Centre",
  :position [84 222],
  :functions
  [{:function "R",
    :start 295372,
    :easing 0,
    :end 296828,
    :arguments [0.261799 0.261799]}
   {:function "S",
    :start 295372,
    :easing 0,
    :end 296828,
    :arguments [50.0 50]}
   {:function "F",
    :start 295372,
    :easing 7,
    :end 296828,
    :arguments [1 0]}]}])

(def lines
    [{:effect lyrics/create-text
  		:effect-parameters
    {:position [320 400]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :line "I am the Father ever waiting\nfor his lost and wayward child"
     :time {:start (+ 193381 85244) :end (+ 85244 198041)}}
   :functions [(partial move/move {:easing 17 :arguments [0 -90] :start -1/1 :end 0})
               (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -5] :start -1/1 :end 0})
               (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 12)] :start -1/1 :end 0 :restriction restrict/rand-posneg})
               (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                           (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                           (partial move/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                           (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                           (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                           ; (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [20 0] :start 4/8 :end 9/8})
               ; (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 20] :start 4/8 :end 9/8})
               ; (partial move/move {:easing 17 :arguments [0 40] :start 4/8 :end 9/8})
               ; (partial rotate/rotate-with-restriction {:easing 17 :arguments [(/ -3.14 15)] :start 4/8 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
               (partial fade/fade-in-and-out {:fade-in-start -1 :fade-in-end -7/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "You cannot see me"
     :time {:start (+ 85244 198041) :end (+ 85244 202701)}}
    :functions [(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                ; (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [20 0] :start 4/8 :end 9/8})
                ; (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 20] :start 4/8 :end 9/8})
                ; (partial move/move {:easing 17 :arguments [0 40] :start 4/8 :end 9/8})
                ; (partial rotate/rotate-with-restriction {:easing 17 :arguments [(/ -3.14 15)] :start 4/8 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "The resurrection and the life"
     :time {:start (+ 85244 202701) :end (+ 85244 207361)}}
     :functions [(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                 (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                 (partial move/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                 (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                 (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                 (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                 ; (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [20 0] :start 4/8 :end 9/8})
                 ; (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 20] :start 4/8 :end 9/8})
                 ; (partial move/move {:easing 17 :arguments [0 40] :start 4/8 :end 9/8})
                 ; (partial rotate/rotate-with-restriction {:easing 17 :arguments [(/ -3.14 15)] :start 4/8 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                 (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "the doorway,"
     :time {:start (+ 85244 207361) :end (+ 85244 209691)}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start -1/8 :fade-in-end 0 :fade-in-easing 0 :fade-out-start 15/16 :fade-out-end 1 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line "and the vine,"
     :time {:start (+ 85244 209691) :end (+ 85244 211439)}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/16 :fade-in-easing 0 :fade-out-start 15/16 :fade-out-end 1 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :line "I AM"
     :scale 0.3
     :time {:start (+ 85244 211439) :end (+ 85244 216681)}}
      :functions [(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                  (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                  (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/16 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :line "You cannot touch me"
     :scale 0.2
     :time {:start (+ 85244 216681) :end (+ 85244 221342)}}
      :functions [(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                  (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                  (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :line "The bread of life, light of the world"
     :scale 0.2
     :time {:start (+ 85244 221342) :end (+ 85244 226002)}}
      :functions [(partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                  (partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                  (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :line "Long before"
     :scale 0.2
     :time {:start (+ 85244 226002) :end (+ 85244 228332)}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start -1/8 :fade-in-end 0 :fade-in-easing 0 :fade-out-start 15/16 :fade-out-end 1 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :line "Abraham"
     :scale 0.25
     :time {:start (+ 85244 228332) :end (+ 85244 230096)}}
      :functions [(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 1 :restriction restrict/rand-posneg})
                  (partial move/move-with-restriction {:restriction restrict/rand-pos :easing 18 :arguments [0 -20] :start 0 :end 1})
                  (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/16 :fade-in-easing 0 :fade-out-start 15/16 :fade-out-end 1 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :line "I AM"
     :scale 0.3
     :time {:start (+ 85244 230096) :end (+ 85244 232792)}}
      :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/16 :fade-in-easing 0 :fade-out-start 16/8 :fade-out-end 16/8 :fade-out-easing 0})
(partial move/move {:easing 17 :arguments [0 -60] :start 0 :end 9/8})]}])


; (def particles
;   {:effect particles/generate-two-dimensional-particles}]

(defn main
  []
  [hand-commands
   (effects/create-effects lines)])
