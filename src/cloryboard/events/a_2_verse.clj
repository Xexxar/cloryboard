(ns cloryboard.events.a-2-verse
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
            [cloryboard.functions.fade :as fade]
            [cloryboard.functions.restrictions :as restrict]
            [cloryboard.effects.particles :as particles]
            [cloryboard.functions.color :as color]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(defn generate-angel
  [start end pos rotation]
  )

(def spectrum
  [{:effect spectrum/create-volume-effect
  	 :effect-parameters 
  	   {:image "sb/circle.png"
  	    :scale 2
  	   	; :image "sb/dot.png"
  	    ; :scale 40
  	    :easing 0
  	    :position [320 170]
  	    :fraction 1/512
  	    :time {:start 562331 :end 599345 }}}])

(def hand-commands
	[{:filepath "sb/backgroundbw.png"
     :type "Sprite"
     :layer "Background"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "S",
                  :start 562331,
                  :easing 0,
                  :end 640173,
                  :arguments [0.6]}]}
   {:filepath "sb/cross-alone.png"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "S",
                  :start 562331,
                  :easing 0,
                  :end 640173,
                  :arguments [0.7]}]}
   {:filepath "sb/cross-alone-purewhite.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 240]
    :functions [{:function "S"
    													:arguments [0.7]
    													:start 560042
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [0 0.125]
    													:start 560037
    													:easing 4
    												 :end 560162}
    												{:function "F"
    													:arguments [0.125 0]
    													:start 560162
    													:easing 3
    												 :end 560281}
    												{:function "F"
    													:arguments [0 0.25]
    													:start 560281
    													:easing 4
    												 :end 560400}
    												{:function "F"
    													:arguments [0.25 0]
    													:start 560400
    													:easing 3
    												 :end 560536}
    												{:function "F"
    													:arguments [0 0.375]
    													:start 560536
    													:easing 4
    												 :end 560672}
    												{:function "F"
    													:arguments [0.375 0]
    													:start 560672
    													:easing 3
    												 :end 560805}
    												{:function "F"
    													:arguments [0 0.5]
    													:start 560805
    													:easing 4
    												 :end 560939}
    												{:function "F"
    													:arguments [0.5 0]
    													:start 560939
    													:easing 3
    												 :end 561070}
    												{:function "F"
    													:arguments [0 0.675]
    													:start 561070
    													:easing 4
    												 :end 561202}
    												{:function "F"
    													:arguments [0.675 0]
    													:start 561202
    													:easing 3
    												 :end 561338}
    												{:function "F"
    													:arguments [0 0.75]
    													:start 561338
    													:easing 4
    												 :end 561474}
    												{:function "F"
    													:arguments [0.75 0]
    													:start 561474
    													:easing 3
    												 :end 561614}
    												{:function "F"
    													:arguments [0 0.875]
    													:start 561614
    													:easing 4
    												 :end 561754}
    												{:function "F"
    													:arguments [0.875 0]
    													:start 561754
    													:easing 3
    												 :end 561895}
    												{:function "F"
    													:arguments [0 1]
    													:start 561895
    													:easing 4
    												 :end 562037}
    												{:function "F"
    													:arguments [1 0]
    													:start 562037
    													:easing 3
    												 :end 562185}]}])


(def lyrics
  [{:effect lyrics/create-text
		:effect-parameters
    {:line "I am humility, the one\nwho laid it all aside"
     :position [320 400]
     :time {:start 562331 :end 566991}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]
		:metadata {:m-easing 1/32}}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "Traded a crown of kings for a crown\nof thorns, betrayed by human pride"
     :position [320 400]
     :time {:start 566991 :end 571651}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]
		:metadata {}}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am humanity, took on\nyour curse and all your pain"
     :position [320 400]
     :time {:start 571651 :end 576311}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})]}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am divinity, eternity,\nforever I remain"
     :position [320 400]
     :time {:start 576311 :end 580971}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]
		:metadata {}}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am your guilty scars,\nas Roman soldiers tear my back"
     :position [320 400]
     :time {:start 580971 :end 585631}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the crimson strain that\n washes all the souls faded to black"
     :position [320 400]
     :time {:start 585631 :end 590291}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the one who bled\nin silence and endured it all"
     :position [320 400]
     :time {:start 590291 :end 594951}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "I am the Word\nwho spoke no word"
     :position [320 400]
     :time {:start 594660 :end 596667}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15}
    :functions [(partial move/move {:easing 18 :arguments [0 -120] :start 0 :end 9/8})
                (partial move/move-random-percent-of-movement {:easing 0 :arguments [0 -20] :start 0 :end 9/8})
                  (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line-non-neg :easing 0 :arguments [0 -20] :start 0 :end 9/8})
                (partial move/move-with-restriction {:restriction restrict/ratio-based-on-dist-from-center-line :easing 0 :arguments [-20 0] :start 0 :end 9/8})
                (partial rotate/rotate-with-restriction {:easing 18 :arguments [(/ 3.14 9)] :start 0 :end 9/8 :restriction restrict/rand-posneg})
                (partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}
	 {:effect lyrics/create-text
 		:effect-parameters
    {:line "With a thousand angels\nwaiting for my call"
     :position [320 240]
     :time {:start 596667 :end 599345}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.2}
    :functions [(partial fade/fade {:start 0 :end 1/9 :easing 1 :arguments [0 1]})]}
                ])

(def special-commands
  [{:filepath "sb/white.jpg"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "F",
                  :start 562331,
                  :easing 7,
                  :end 564369,
                  :arguments [1 0]}]}
   {:filepath "sb/white.jpg"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 240]
    :functions [{:function "F",
                 :start 596667,
                 :easing 17,
                 :end 596944,
                 :arguments [0 0.5]}
                {:function "C"
                 :start 596667
                 :end 599345
                 :easing 0
                 :arguments [0 0 0 0 0 0]}]}])

(def spectrum2
  [{:effect spectrum/create-volume-effect
  	 :effect-parameters 
  	   {:image "sb/circle.png"
  	    :scale 2
  	   	; :image "sb/dot.png"
  	    ; :scale 40
  	    :easing 0
  	    :position [320 170]
  	    :fraction 1/512
  	    :time {:start 599345 :end 632448}}}])

(def particles
  [{:effect particles/create-particle-routine
    :effect-parameters
    {:count 200
     :scale-range [0.25 1]
     :files ["sb/dot.png"]
     :time  {:start 562331 :end 599345}
     :movements [{:easing 0 :start 0 :end 1 :arguments [0 1000]}]}
    :functions [(partial color/color {:start 0 :end 1 :easing 0 :arguments [210 10 10 210 10 10]})]}])

(defn main
  []
  [(effects/create-effects spectrum)
   (effects/create-effects spectrum2)
   hand-commands
   (effects/create-effects particles)
   special-commands
   (effects/create-effects lyrics)])