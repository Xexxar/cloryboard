(ns cloryboard.events.4_chorus1
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.common.effects :as effects]
            [cloryboard.functions.common :as func-common]
            [cloryboard.functions.move :as func-movement]
            [cloryboard.functions.rotate :as func-rotate]
            [cloryboard.functions.fade :as func-fade]
            [cloryboard.functions.restrictions :as restrict]
            [cloryboard.effects.particles :as particles]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(def hand-commands
  [{:filepath "sb/bible.png"
     :type "Sprite"
     :layer "Background"
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
                  :arguments [320 480 320 620]}
                  ]}])

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
   :functions [(partial func-movement/move {:easing 17 :arguments [0 -90] :start -1/1 :end 0})
               (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                           (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                           (partial func-movement/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                           (partial func-movement/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                           (partial func-rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                           ; (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [20 0] :start 4/8 :end 9/8})
               ; (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 20] :start 4/8 :end 9/8})
               ; (partial func-movement/move {:easing 17 :arguments [0 40] :start 4/8 :end 9/8})
               ; (partial func-rotate/rotate-with-restriction {:easing 17 :arguments [(/ -3.14 15)] :start 4/8 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
               (partial func-fade/fade-in-and-out {:fade-in-start -1 :fade-in-end -7/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "You cannot see me"
     :time {:start 198041 :end 202701}}
    :functions [(partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial func-movement/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                (partial func-movement/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial func-rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                ; (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [20 0] :start 4/8 :end 9/8})
                ; (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 20] :start 4/8 :end 9/8})
                ; (partial func-movement/move {:easing 17 :arguments [0 40] :start 4/8 :end 9/8})
                ; (partial func-rotate/rotate-with-restriction {:easing 17 :arguments [(/ -3.14 15)] :start 4/8 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                (partial func-fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "The resurrection and the life"
     :time {:start 202701 :end 207361}}
     :functions [(partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                 (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                 (partial func-movement/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                 (partial func-movement/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                 (partial func-rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                 ; (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [20 0] :start 4/8 :end 9/8})
                 ; (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 20] :start 4/8 :end 9/8})
                 ; (partial func-movement/move {:easing 17 :arguments [0 40] :start 4/8 :end 9/8})
                 ; (partial func-rotate/rotate-with-restriction {:easing 17 :arguments [(/ -3.14 15)] :start 4/8 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                 (partial func-fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2
     :line "the doorway, and the vine,"
     :time {:start 207361 :end 211876}}
      :functions [(partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                  (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial func-movement/move {:easing 18 :arguments [0 -80] :start 0 :end 9/8})
                  (partial func-movement/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial func-rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 15)] :start 0 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  ; (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [20 0] :start 4/8 :end 9/8})
                  ; (partial func-movement/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 20] :start 4/8 :end 9/8})
                  ; (partial func-movement/move {:easing 17 :arguments [0 40] :start 4/8 :end 9/8})
                  ; (partial func-rotate/rotate-with-restriction {:easing 17 :arguments [(/ -3.14 15)] :start 4/8 :end 9/8 :restriction restrict/ratio-based-on-dist-from-center-line})
                  (partial func-fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})]}
    {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 310]
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :line "I am"
     :scale 0.3
     :time {:start 212021 :end 216681}}
      :functions [(partial func-fade/fade-in-and-out {:fade-in-start -1/8 :fade-in-end 0 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 0})
(partial func-movement/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})]}])


; (def particles
;   {:effect particles/generate-two-dimensional-particles}]

(defn main
  []
  [hand-commands
   (effects/create-effects lines)])
