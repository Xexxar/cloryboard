(ns cloryboard.events.4_chorus1
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
  [{:filepath "sb/alphabw/lightning1.png"
   :type "Sprite"
   :layer "Foreground"
   :tether "Centre"
   :position [-30 280]
   :functions [{:function "S",
                :start 228332,
                :easing 0,
                :end 229802,
                :arguments [1.25]}
               {:function "R",
                :start 228773,
                :easing 0,
                :end 229802,
                :arguments [0.261799 0.261799]}
               {:function "F",
                :start 228332,
                :easing 0,
                :end 229802,
                :arguments [1 0]}]}
  {:filepath "sb/alphabw/lightning4.png"
   :type "Sprite"
   :layer "Foreground"
   :tether "Centre"
   :position [460 280]
   :functions [{:function "S",
                :start 228773,
                :easing 0,
                :end 230238,
                :arguments [1.25]}
               {:function "R",
                :start 228773,
                :easing 0,
                :end 230238,
                :arguments [-0.261799 -0.261799]}
               {:function "F",
                :start 228773,
                :easing 0,
                :end 230238,
                :arguments [1 0]}]}
  {:filepath "sb/rol.png"
   :type "Sprite"
   :layer "Foreground"
   :tether "Centre"
   :position [530 260]
   :functions [{:function "R",
                :start 209691,
                :easing 0,
                :end 211147,
                :arguments [3.64 3.64]}
               {:function "S",
                :start 209691,
                :easing 0,
                :end 211147,
                :arguments [1.25]}
               {:function "F",
                :start 209691,
                :easing 0,
                :end 211147,
                :arguments [1 0]}]}
  {:filepath "sb/rol3.png"
   :type "Sprite"
   :layer "Foreground"
   :tether "Centre"
   :position [120 260]
   :functions [{:function "R",
                :start 210128,
                :easing 0,
                :end 211584,
                :arguments [2.74 2.74]}
               {:function "S",
                :start 210128,
                :easing 0,
                :end 211584,
                :arguments [1.25]}
               {:function "F",
                :start 210128,
                :easing 0,
                :end 211584,
                :arguments [1 0]}]}
  {:filepath "sb/bible.png"
     :type "Sprite"
     :layer "Foreground"
     :tether "BottomCentre"
     :position [320 680]
     :functions [{:function "S",
                  :start 193381,
                  :easing 0,
                  :end 229508,
                  :arguments [0.5 0.5]}
                 {:function "M",
                  :start 193381,
                  :easing 19,
                  :end 198041,
                  :arguments [320 620 320 480]}
                 {:function "M",
                  :start 229508,
                  :easing 18,
                  :end 232792,
                  :arguments [320 480 320 620]}]}
  {:filepath "sb/glow.png"
   :type "Sprite"
   :layer "Foreground"
   :tether "Centre"
   :position [84 222]
   :functions [{:function "R",
                :start 228332,
                :easing 0,
                :end 229214,
                :arguments [-0.261799, -0.261799]}
               {:function "S",
                :start 228332,
                :easing 0,
                :end 229214,
                :arguments [50.0, 50]}
               {:function "F",
                :start 228332,
                :easing 7,
                :end 229214,
                :arguments [1 0]}]}
{:filepath "sb/glow.png"
 :type "Sprite"
 :layer "Foreground"
 :tether "Centre"
 :position [490 222]
 :functions [{:function "R",
              :start 228773,
              :easing 0,
              :end 230238,
              :arguments [0.261799, 0.261799]}
             {:function "S",
              :start 228773,
              :easing 0,
              :end 230238,
              :arguments [50.0, 50]}
             {:function "F",
              :start 228773,
              :easing 7,
              :end 230238,
              :arguments [1 0]}]}
{:filepath "sb/glow.png"
 :type "Sprite"
 :layer "Foreground"
 :tether "Centre"
 :position [490 222]
 :functions [{:function "R",
              :start 209691,
              :easing 0,
              :end 211147,
              :arguments [-0.261799, -0.261799]}
             {:function "S",
              :start 209691,
              :easing 0,
              :end 211147,
              :arguments [50.0, 50]}
             {:function "F",
              :start 209691,
              :easing 7,
              :end 211147,
              :arguments [1 0]}]}
{:filepath "sb/glow.png"
 :type "Sprite"
 :layer "Foreground"
 :tether "Centre"
 :position [84 222]
 :functions [{:function "R",
            :start 210128,
            :easing 0,
            :end 211584,
            :arguments [0.261799, 0.261799]}
           {:function "S",
            :start 210128,
            :easing 0,
            :end 211584,
            :arguments [50.0, 50]}
           {:function "F",
            :start 210128,
            :easing 7,
            :end 211584,
            :arguments [1 0]}]}])

(def lines
    [{:effect lyrics/create-text
  		:effect-parameters
    {:position [320 400]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :line "I am the Alpha and Omega,\nfirst and last, eternally"
     :time {:start 193381 :end 198041}}
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
     :time {:start 198041 :end 202701}}
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
     :time {:start 202701 :end 207361}}
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
     :time {:start 207361 :end 209691}}
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
     :time {:start 209691 :end 211439}}
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
     :time {:start 211439 :end 216681}}
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
     :time {:start 216681 :end 221342}}
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
     :time {:start 221342 :end 226002}}
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
     :time {:start 226002 :end 228332}}
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
     :time {:start 228332 :end 230096}}
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
     :time {:start 230096 :end 232792}}
      :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/16 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})
(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
(partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
(partial rotate/rotate-with-restriction {:easing 0 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
(partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})]}])


; (def particles
;   {:effect particles/generate-two-dimensional-particles}]

(defn main
  []
  [hand-commands
   (effects/create-effects lines)])
