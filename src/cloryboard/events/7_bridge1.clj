(ns cloryboard.events.7-bridge1
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.common.effects :as effects]
            [cloryboard.functions.common :as common]
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
	[{:filepath "sb/iamtri.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 260]
    :functions [{:function "S"
    													:arguments [2 0.25]
    													:start 345393
    													:easing 8
    												 :end 347048}
    												{:function "R"
                 :start 345393
                 :end 386772
                 :easing 0
                 :arguments [3.141592]}
                {:function "S"
                 :start 345393
                 :end 386359
                 :easing 0
                 :arguments [0.25]}
                {:function "S"
                 :start 386359
                 :end 387186
                 :easing 9
                 :arguments [0.25 2]}]}])

(def left-line1
		[{:start 0
				 :end 1/3
				 :line "I"}
				{:start 1/3
				 :end 2/3
				 :line "am"}
				{:start 2/3
				 :end 3/3
				 :line "the"}
				{:start 3/3
				 :end 4/3
				 :line "heart"}
				{:start 4/3
				 :end 5/3
				 :line "of"}
				{:start 5/3
				 :end 6/3
				 :line "the"}
				{:start 6/3
				 :end 8/3
				 :line "righteous"}
				{:start 8/3
				 :end 10/3
				 :line "desire"}
				{:start 10/3
				 :end 11/3
				 :line "and"}
				{:start 11/3
				 :end 12/3
				 :line "the"}
				{:start 12/3
				 :end 13/3
				 :line "fourth"}
				{:start 13/3
				 :end 14/3
				 :line "man"}
				{:start 14/3
				 :end 15/3
				 :line "you"}
				{:start 15/3
				 :end 16/3
				 :line "see"}
				{:start 16/3
				 :end 17/3
				 :line "in"}
				{:start 17/3
				 :end 18/3
				 :line "the"}
				{:start 18/3
				 :end 19/3
				 :line "midst"}
				{:start 19/3
				 :end 20/3
				 :line "of"}
				{:start 20/3
				 :end 21/3
				 :line "the"}
				{:start 21/3
				 :end 24/3
				 :line "fire"}
				{:start 24/3
				 :end 25/3
				 :line "I"}
				{:start 25/3
				 :end 26/3
				 :line "am"}
				{:start 26/3
				 :end 27/3
				 :line "the"}
				{:start 27/3
				 :end 29/3
				 :line "giver"}
				{:start 29/3
				 :end 30/3
				 :line "of"}
				{:start 30/3
				 :end 31/3
				 :line "life"}
				{:start 31/3
				 :end 32/3
				 :line "and"}
				{:start 32/3
				 :end 33/3
				 :line "the"}
				{:start 33/3
				 :end 35/3
				 :line "promise"}
				{:start 35/3
				 :end 36/3
				 :line "of"}
				{:start 36/3
				 :end 45/3
				 :line "Israel"}])

(def left-line1
		[{:start 0
				 :end 1/3
				 :line "I"}
				{:start 1/3
				 :end 2/3
				 :line "am"}
				{:start 2/3
				 :end 3/3
				 :line "the"}
				{:start 3/3
				 :end 4/3
				 :line "heart"}
				{:start 4/3
				 :end 5/3
				 :line "of"}
				{:start 5/3
				 :end 6/3
				 :line "the"}
				{:start 6/3
				 :end 8/3
				 :line "righteous"}
				{:start 8/3
				 :end 10/3
				 :line "desire"}
				{:start 10/3
				 :end 11/3
				 :line "and"}
				{:start 11/3
				 :end 12/3
				 :line "the"}
				{:start 12/3
				 :end 13/3
				 :line "fourth"}
				{:start 13/3
				 :end 14/3
				 :line "man"}
				{:start 14/3
				 :end 15/3
				 :line "you"}
				{:start 15/3
				 :end 16/3
				 :line "see"}
				{:start 16/3
				 :end 17/3
				 :line "in"}
				{:start 17/3
				 :end 18/3
				 :line "the"}
				{:start 18/3
				 :end 19/3
				 :line "midst"}
				{:start 19/3
				 :end 20/3
				 :line "of"}
				{:start 20/3
				 :end 21/3
				 :line "the"}
				{:start 21/3
				 :end 24/3
				 :line "fire"}
				{:start 24/3
				 :end 25/3
				 :line "I"}
				{:start 25/3
				 :end 26/3
				 :line "am"}
				{:start 26/3
				 :end 27/3
				 :line "the"}
				{:start 27/3
				 :end 29/3
				 :line "giver"}
				{:start 29/3
				 :end 30/3
				 :line "of"}
				{:start 30/3
				 :end 31/3
				 :line "life"}
				{:start 31/3
				 :end 32/3
				 :line "and"}
				{:start 32/3
				 :end 33/3
				 :line "the"}
				{:start 33/3
				 :end 35/3
				 :line "promise"}
				{:start 35/3
				 :end 36/3
				 :line "of"}
				{:start 36/3
				 :end 45/3
				 :line "Israel"}])

(def left-line2
		[{:start 0
				 :end 1/3
				 :line "I"}
				{:start 1/3
				 :end 2/3
				 :line "am"}
				{:start 2/3
				 :end 3/3
				 :line "the"}
				{:start 3/3
				 :end 4/3
				 :line "hope"}
				{:start 4/3
				 :end 5/3
				 :line "of"}
				{:start 5/3
				 :end 6/3
				 :line "the"}
				{:start 6/3
				 :end 8/3
				 :line "lonely"}
				{:start 8/3
				 :end 9/3
				 :line "and"}
				{:start 9/3
				 :end 10/3
				 :line "lost"}
				{:start 10/3
				 :end 11/3
				 :line "and"}
				{:start 11/3
				 :end 12/3
				 :line "the"}
				{:start 12/3
				 :end 13/3
				 :line "blood"}
				{:start 13/3
				 :end 15/3
				 :line "running"}
				{:start 15/3
				 :end 16/3
				 :line "down"}
				{:start 16/3
				 :end 17/3
				 :line "to"}
				{:start 17/3
				 :end 18/3
				 :line "the"}
				{:start 18/3
				 :end 19/3
				 :line "foot"}
				{:start 19/3
				 :end 20/3
				 :line "of"}
				{:start 20/3
				 :end 21/3
				 :line "the"}
				{:start 21/3
				 :end 24/3
				 :line "cross"}
				{:start 24/3
				 :end 25/3
				 :line "I"}
				{:start 25/3
				 :end 26/3
				 :line "am"}
				{:start 26/3
				 :end 27/3
				 :line "the"}
				{:start 27/3
				 :end 29/3
				 :line "breaking"}
				{:start 29/3
				 :end 30/3
				 :line "of"}
				{:start 30/3
				 :end 31/3
				 :line "chains"}
				{:start 31/3
				 :end 32/3
				 :line "and"}
				{:start 32/3
				 :end 33/3
				 :line "the"}
				{:start 33/3
				 :end 35/3
				 :line "tolling"}
				{:start 35/3
				 :end 36/3
				 :line "of"}
				{:start 36/3
				 :end 42/3
				 :line "freedom's"}
			 {:start 42/3
				 :end 45/3
				 :line "bell"}])


; "I am the notes that eternally ring in the music too holy for angels to sing\nI am the fire on the altar consuming the sacrifice"
(def left-line3
		[{:start 0
				 :end 1/3
				 :line "I"}
				{:start 1/3
				 :end 2/3
				 :line "am"}
				{:start 2/3
				 :end 3/3
				 :line "the"}
				{:start 3/3
				 :end 4/3
				 :line "notes"}
				{:start 4/3
				 :end 5/3
				 :line "that"}
				{:start 5/3
				 :end 9/3
				 :line "eternally"}
				{:start 9/3
				 :end 10/3
				 :line "ring"}
				{:start 10/3
				 :end 11/3
				 :line "and"}
				{:start 11/3
				 :end 12/3
				 :line "the"}
				{:start 12/3
				 :end 14/3
				 :line "music"}
				{:start 14/3
				 :end 15/3
				 :line "too"}
				{:start 15/3
				 :end 17/3
				 :line "holy"}
				{:start 17/3
				 :end 18/3
				 :line "for"}
				{:start 18/3
				 :end 20/3
				 :line "angels"}
				{:start 20/3
				 :end 21/3
				 :line "to"}
				{:start 21/3
				 :end 24/3
				 :line "sing"}
				{:start 24/3
				 :end 25/3
				 :line "I"}
				{:start 25/3
				 :end 26/3
				 :line "am"}
				{:start 26/3
				 :end 27/3
				 :line "the"}
				{:start 27/3
				 :end 28/3
				 :line "fire"}
				{:start 28/3
				 :end 29/3
				 :line "on"}
				{:start 29/3
				 :end 30/3
				 :line "the"}
				{:start 30/3
				 :end 32/3
				 :line "altar"}
				{:start 32/3
				 :end 35/3
				 :line "consuming"}
				{:start 35/3
				 :end 36/3
				 :line "the"}
				{:start 36/3
				 :end 45/3
				 :line "sacrifice"}])

;; "I am the three and yet I am the one in the grace of the Father and death of the Son\nI am the one who redeemed you by paying the highest price"

(def left-line4
		[{:start 0
				 :end 1/3
				 :line "I"}
				{:start 1/3
				 :end 2/3
				 :line "am"}
				{:start 2/3
				 :end 3/3
				 :line "the"}
				{:start 3/3
				 :end 4/3
				 :line "three"}
				{:start 4/3
				 :end 5/3
				 :line "and"}
				{:start 5/3
				 :end 6/3
				 :line "yet"}
				{:start 6/3
				 :end 7/3
				 :line "I"}
				{:start 7/3
				 :end 8/3
				 :line "am"}
				{:start 8/3
				 :end 9/3
				 :line "the"}
				{:start 9/3
				 :end 10/3
				 :line "one"}
				{:start 10/3
				 :end 11/3
				 :line "in"}
				{:start 11/3
				 :end 12/3
				 :line "the"}
				{:start 12/3
				 :end 13/3
				 :line "grace"}
				{:start 13/3
				 :end 14/3
				 :line "of"}
				{:start 14/3
				 :end 15/3
				 :line "the"}
				{:start 15/3
				 :end 17/3
				 :line "father"}
				{:start 17/3
				 :end 18/3
				 :line "and"}
				{:start 18/3
				 :end 19/3
				 :line "death"}
				{:start 19/3
				 :end 20/3
				 :line "of"}
				{:start 20/3
				 :end 21/3
				 :line "the"}
				{:start 21/3
				 :end 24/3
				 :line "Son"}
				{:start 24/3
				 :end 25/3
				 :line "I"}
				{:start 25/3
				 :end 26/3
				 :line "am"}
				{:start 26/3
				 :end 27/3
				 :line "the"}
				{:start 27/3
				 :end 28/3
				 :line "one"}
				{:start 28/3
				 :end 29/3
				 :line "who"}
				{:start 29/3
				 :end 31/3
				 :line "redeemed"}
				{:start 31/3
				 :end 32/3
				 :line "you"}
				{:start 32/3
				 :end 33/3
				 :line "by"}
				{:start 33/3
				 :end 35/3
				 :line "paying"}
				{:start 35/3
				 :end 36/3
				 :line "the"}
				{:start 36/3
				 :end 42/3
				 :line "highest"}
			 {:start 42/3
				 :end 45/3
				 :line "price"}])


(def right-line1
		[{:start 0
				:end 2
				:line "I"}
			{:start 2
				:end 4
				:line "am"}
			{:start 4
				:end 5
				:line "grace"}
			{:start 5
				:end 6
				:line "and"}
			{:start 6
				:end 8
				:line "mercy"}
			{:start 8
				:end 10
				:line "I"}
			{:start 10
				:end 12
				:line "am"}
			{:start 12
				:end 15
				:line "sacrifice"}])

(def right-line2
		[{:start 0
				:end 2
				:line "I"}
			{:start 2
				:end 4
				:line "am"}
			{:start 4
				:end 6
				:line "endless"}
			{:start 6
				:end 8
				:line "glory"}
			{:start 8
				:end 10
				:line "I"}
			{:start 10
				:end 12
				:line "am"}
			{:start 12
				:end 13
				:line "light"}
			{:start 13
				:end 14
				:line "and"}
			{:start 14
				:end 15
				:line "life"}])

(def right-line3
		[{:start 0
				:end 2
				:line "I"}
			{:start 2
				:end 4
				:line "am"}
			{:start 4
				:end 5
				:line "long"}
			{:start 5
				:end 8
				:line "awaited"}
			{:start 8
				:end 10
				:line "hope"}
			{:start 10
				:end 12
				:line "of"}
			{:start 12
				:end 15
				:line "Israel"}])

(def right-line4
		[{:start 0
				:end 2
				:line "I"}
			{:start 2
				:end 4
				:line "am"}
			{:start 4
				:end 6
				:line "longing"}
			{:start 6
				:end 7
				:line "sated"}
			{:start 8
				:end 12
				:line "prophecy"}
			{:start 12
				:end 15
				:line "fulfilled"}])

(def center-line1
		[{:start 0
				:end 3/3
				:line "I"}
			{:start 3/3
				:end 9/3
				:line "am"}
			{:start 9/3
				:end 10/3
				:line "from"}
			{:start 10/3
				:end 11/3
				:line "the"}
			{:start 11/3
				:end 14/3
				:line "beginning"}
			{:start 14/3
				:end 15/3
				:line "of"}
			{:start 15/3
				:end 21/3
				:line "time"}
			{:start 21/3
				:end 24/3
				:line "and"}
			{:start 24/3
				:end 27/3
				:line "I"}
			{:start 27/3
				:end 35/3
				:line "am"}
			{:start 35/3
				:end 38/3
				:line "sustaining"}
			{:start 38/3
				:end 39/3
				:line "the"}
			{:start 39/3
				:end 41/3
				:line "system"}
			{:start 41/3
				:end 42/3
				:line "of"}
			{:start 42/3
				:end 45/3
				:line "life"}])

(def center-line2
		[{:start 0
				:end 3/3
				:line "I"}
			{:start 3/3
				:end 9/3
				:line "am"}
			{:start 9/3
				:end 12/3
				:line "symmetry"}
			{:start 12/3
				:end 14/3
				:line "reason"}
			{:start 14/3
				:end 15/3
				:line "and"}
			{:start 15/3
				:end 21/3
				:line "rhyme"}
			{:start 21/3
				:end 24/3
				:line "and"}
			{:start 24/3
				:end 27/3
				:line "I"}
			{:start 27/3
				:end 35/3
				:line "am"}
			{:start 35/3
				:end 38/3
				:line "conviction"}
			{:start 38/3
				:end 39/3
				:line "that"}
			{:start 39/3
				:end 40/3
				:line "cuts"}
			{:start 40/3
				:end 41/3
				:line "like"}
			{:start 41/3
				:end 42/3
				:line "a"}
			{:start 42/3
				:end 45/3
				:line "knife"}])

; "I am the heart of the righteous desire and the fourth man you see in the midst of the fire\nI am the giver of life and the promise of Israel"
; "I am the hope of the lonely and lost in the blood running down to the foot of the cross\nI am the breaking of chains and the tolling of freedom's bell"
; "I am the notes that eternally ring in the music too holy for angels to sing\nI am the fire on the altar consuming the sacrifice"
; 

(def left-lines
		(reduce into 
		[(mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [500 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 347048 (* (get arg :start) (- 347462 347048))) :end (+ 347048 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   left-line1)
		(mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [500 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 353669 (* (get arg :start) (- 347462 347048))) :end (+ 353669 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   left-line2)
	 (mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [500 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 360290 (* (get arg :start) (- 347462 347048))) :end (+ 360290 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   left-line3)
	 (mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [500 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 366910 (* (get arg :start) (- 347462 347048))) :end (+ 366910 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   left-line4)
	 (mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [500 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 373531 (* (get arg :start) (- 347462 347048))) :end (+ 373531 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   left-line1)
	 (mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [500 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 380152 (* (get arg :start) (- 347462 347048))) :end (+ 380152 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   left-line2)]))

(def right-lines
		(reduce into 
		[(mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [140 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 360290 (* (get arg :start) (- 347462 347048))) :end (+ 360290 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   right-line1)
		(mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [140 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 366910 (* (get arg :start) (- 347462 347048))) :end (+ 366910 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   right-line2)
		(mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [140 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 373531 (* (get arg :start) (- 347462 347048))) :end (+ 373531 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   right-line3)
		(mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [140 110]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 380152 (* (get arg :start) (- 347462 347048))) :end (+ 380152 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   right-line4)]))

(def center-lines
		(reduce into 
		[(mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 420]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 373531 (* (get arg :start) (- 347462 347048))) :end (+ 373531 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   center-line1)
		(mapv 
		(fn [arg]
		 {:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 420]
     :text-offsets {:h 15 :v 0}
     :align "Centre"
     :tether "Centre"
     :scale 0.25
     :line (get arg :line)
     :time {:start (+ 380152 (* (get arg :start) (- 347462 347048))) :end (+ 380152 (* (get arg :end) (- 347462 347048)))}}
    :functions []})
   center-line2)]))

(def lyrics
	[{:effect lyrics/create-text
  	 :effect-parameters
    {:position [320 256]
     :text-offsets {:h 15 :v 100}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :line "Wherefore it is written,\n\"God opposes the proud but gives grace to the humble.\"\nJames 4:6"
     :time {:start 320566 :end 323876}}
    :functions [(partial fade/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/2 :fade-in-easing 1 :fade-out-start 1/1 :fade-out-end 3/2 :fade-out-easing 1})]}
   ])

(def particles
  [{:effect particles/create-box-of-particles
    :effect-parameters
    {:count 200
     :scale-range [0.25 0.25]
     :files ["sb/dot.png"]
     :time  {:start 320566 :end 347048}
     :coords [[-107 0] [747 520]]}
    :functions (conj
    													(mapv #(if (odd? %)
    																								(partial move/move {:start (/ % 2) :easing 5 :end (+ (/ % 2) 1) :arguments [0 40]}) 
    																								(partial move/move {:start (/ % 2) :easing 5 :end (+ (/ % 2) 1) :arguments [0 -40]}))
    																								(range 9))
    													(partial fade/fade-in-and-out {:fade-in-start 1/8 :fade-in-end 1/4 :fade-in-easing 1 :fade-out-start 71/16 :fade-out-end 72/16 :fade-out-easing 1}))}])

(defn main
  []
  [hand-commands
  	(effects/create-effects left-lines)
  	(effects/create-effects right-lines)
  	(effects/create-effects center-lines)
   (effects/create-effects lyrics)
   (effects/create-effects particles)])
