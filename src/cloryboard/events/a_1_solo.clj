(ns cloryboard.events.a-1-solo
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
	[{:filepath "sb/white.jpg"
     :type "Sprite"
     :layer "Foreground"
     :tether "Centre"
     :position [320 240]
     :functions [{:function "F",
                  :start 474744,
                  :easing 7,
                  :end 476458,
                  :arguments [1 0]}]}
	 {:filepath "sb/iamtricentered.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 240]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 12
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [320 240 320 250]}
    												             {:function "M"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [320 250 320 240]}]}]}
  {:filepath "sb/i.png"
    :type "Sprite"
    :layer "Foreground" 
    :tether "Centre"
    :position [320 202]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}]}
  {:filepath "sb/am.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "Centre"
    :position [320 257]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}]}
   {:filepath "sb/iamwing.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "BottomLeft"
    :position [373 240]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 12
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [373 240 373 235]}
    												             {:function "M"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [373 235 373 240]}
    												             {:function "R"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI 6)]}
    												             {:function "R"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI 6) 0]}]}]}
   {:filepath "sb/iamwing.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "BottomRight"
    :position [267 240]
    :functions [{:function "V"
    													:arguments [-0.25 0.25 -0.25 0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 12
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 17
    												              :arguments [267 240 267 235]}
    												             {:function "M"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 17
    												              :arguments [267 235 267 240]}
    												             {:function "R"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 17
    												              :arguments [0 (/ Math/PI -6)]}
    												             {:function "R"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 17
    												              :arguments [(/ Math/PI -6) 0]}]}]}
   {:filepath "sb/iamdove2.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "CentreLeft"
    :position [480 180]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 6
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [480 180 540 200]}
    												             {:function "M"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [540 200 540 160]}
    												             {:function "M"
    												              :start (- 481601 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [540 160 540 200]}
    												             {:function "M"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [540 200 480 180]}
    												             {:function "R"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI 12)] }
    												             {:function "R"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI 12) 0]}
    												             {:function "R"
    												              :start (- 481601 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI 12)] }
    												             {:function "R"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI 12) 0]}]}]}
   {:filepath "sb/iamdove2.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "CentreRight"
    :position [160 180]
    :functions [{:function "V"
    													:arguments [-0.25 0.25 -0.25 0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 6
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [160 180 100 200]}
    												             {:function "M"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [100 200 100 160]}
    												             {:function "M"
    												              :start (- 481601 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [100 160 100 200]}
    												             {:function "M"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [100 200 160 180]}
    												             {:function "R"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI -12)] }
    												             {:function "R"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI -12) 0]}
    												             {:function "R"
    												              :start (- 481601 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI -12)] }
    												             {:function "R"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI -12) 0]}]}]}
   {:filepath "sb/iamdove1.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "TopLeft"
    :position [380 80]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 6
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [380 80 360 120]}
    												             {:function "M"
    												              :start (- 478172 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [360 120 420 40]}
    												             {:function "M"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [420 40 380 80]}
    												             {:function "R"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI -12)]}
    												             {:function "R"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI -12) 0]}
    												             {:function "R"
    												              :start (- 481601 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI -12)]}
    												             {:function "R"
    												              :start (- 485029 474744)
    												              :end (- 486744 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI -12) 0]}]}]}
   {:filepath "sb/iamdove1.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "TopRight"
    :position [260 80]
    :functions [{:function "V"
    													:arguments [-0.25 0.25 -0.25 0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 6
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [260 80 280 120]}
    												             {:function "M"
    												              :start (- 478172 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [280 120 220 40]}
    												             {:function "M"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [220 40 260 80]}
    												             {:function "R"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI 12)]}
    												             {:function "R"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI 12) 0]}
    												             {:function "R"
    												              :start (- 481601 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI 12)]}
    												             {:function "R"
    												              :start (- 485029 474744)
    												              :end (- 486744 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI 12) 0]}]}]}
   {:filepath "sb/iamdove3.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "CentreLeft"
    :position [460 280]
    :functions [{:function "S"
    													:arguments [0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 6
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 479886 474744)
    												              :easing 5
    												              :arguments [460 280 500 320]}
    												             {:function "M"
    												              :start (- 479886 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [500 320 480 340]}
    												             {:function "M"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [480 340 460 280]}
    												             {:function "R"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI 12)] }
    												             {:function "R"
    												              :start (- 478172 474744)
    												              :end (- 481601 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI 12) 0]}
    												             {:function "R"
    												              :start (- 481601 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI 12)] }
    												             {:function "R"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI 12) 0]}]}]}
   {:filepath "sb/iamdove3.png"
    :type "Sprite"
    :layer "Foreground"
    :tether "CentreRight"
    :position [180 280]
    :functions [{:function "V"
    													:arguments [-0.25 0.25 -0.25 0.25]
    													:start 474744
    													:easing 0
    												 :end 560162}
    												{:function "F"
    													:arguments [1 0]
    													:start 556466
    													:easing 17
    												 :end 560162}
    												{:function "L"
    												 :start 474744
    												 :count 6
    												 :arguments [{:function "M"
    												              :start (- 474744 474744)
    												              :end (- 479886 474744)
    												              :easing 5
    												              :arguments [180 280 130 320]}
    												             {:function "M"
    												              :start (- 479886 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [130 320 150 340]}
    												             {:function "M"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [150 340 180 280]}
    												             {:function "R"
    												              :start (- 474744 474744)
    												              :end (- 478172 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI -12)] }
    												             {:function "R"
    												              :start (- 478172 474744)
    												              :end (- 481601  474744)
    												              :easing 5
    												              :arguments [(/ Math/PI -12) 0]}
    												             {:function "R"
    												              :start (- 481601 474744)
    												              :end (- 485029 474744)
    												              :easing 5
    												              :arguments [0 (/ Math/PI -12)] }
    												             {:function "R"
    												              :start (- 485029 474744)
    												              :end (- 488351 474744)
    												              :easing 5
    												              :arguments [(/ Math/PI -12) 0]}]}]}])

(def particles
  [{:effect particles/create-particle-routine
    :effect-parameters
    {:count 200
     :scale-range [0.25 1]
     :files ["sb/dot.png"]
     :time  {:start 474744 :end 502172}
     :movements [{:easing 0 :start 0 :end 7/8 :arguments [0 -3500]}
                 {:easing 4 :start 7/8 :end 9/8 :arguments [0 -500]}
                 {:easing 3 :start 9/8 :end 10/8 :arguments [0 100]}
                 {:easing 0 :start 10/8 :end 14/8 :arguments [0 800]}
                 {:easing 4 :start 14/8 :end 15/8 :arguments [0 100]}
                 {:easing 3 :start 15/8 :end 17/8 :arguments [0 -500]}
                 {:easing 0 :start 17/8 :end 23/8 :arguments [0 -3000]}
                 {:easing 4 :start 23/8 :end 25/8 :arguments [0 -500]}]}
    :functions [(partial fade/fade-if-time {:arguments [1 0] :cond-time 25/8 :start 24/8 :end 25/8 :easing 17})]}])

(defn main
  []
  [(effects/create-effects particles)
   hand-commands])