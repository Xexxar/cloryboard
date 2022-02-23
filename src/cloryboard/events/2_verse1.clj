(ns cloryboard.events.2_verse1
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.functions.common :as func-common]
            [cloryboard.functions.move :as func-movement]
            [clojure.java.io :as io]
            [cloryboard.effects.particles :as particles])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(def lines
  {:verse1
    {:line "I am the light upon your path \nwhen you have lost your way"
     :position [320 400]
     :time {:start 107053 :end 111853}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :metadata {:m-easing 1/32}
     :scale 0.15
     :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
                 (partial func-movement/move {:easing 17 :movement [0 -100] :start 0 :end 1})
                 (partial func-movement/move {:easing 16 :movement [-100 0] :start 0 :end 1/4})
                 (partial func-movement/move {:easing 17 :movement [200 0] :start 1/4 :end 3/4})
                 (partial func-movement/move {:easing 15 :movement [-100 0] :start 3/4 :end 1})]}
   :verse2
    {:line "I am the footprints in the sand \nthe ocean's tide can't wash away"
     :position [320 400]
     :time {:start 111853 :end 116653}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})
(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})

                  ]}
   :verse3
    {:line "I am the shelter from the storm \nthat rages on and on"
     :position [320 400]
     :time {:start 116653 :end 121453}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})
(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})]}
   :verse4
    {:line "The incorruptible foundation \nthat the wise man builds upon"
     :position [320 400]
     :time {:start 121453 :end 126253}
     :text-offsets {:h 15 :v 5}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})
(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
]}
   :verse5
    {:line "I am the bread that feeds a \nstarving man upon the street"
     :position [320 400]
     :time {:start 126253 :end 131053}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})
(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
]}
   :verse6
    {:line "I am the bounty on the table \nin the palace at the feast"
     :position [320 400]
     :time {:start 131053 :end 135853}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})
(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}
   :verse7
    {:line "I am the rain upon the earth \nafter a scorching drought"
     :position [320 400]
     :time {:start 135853 :end 140653}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})
(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})]}
   :verse8
    {:line "I am the quenching of the thirst \nyou never thought you'd be without"
     :position [320 400]
     :time {:start 140653 :end 145453}
     :text-offsets {:h 15 :v 50}
     :align "Centre"
     :tether "Centre"
     :scale 0.15
     :functions [(partial func-movement/move {:easing 4 :movement [0 -140] :start 1 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 1 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [-80 -80] :start 9/8 :end 10/8})
(partial func-movement/move {:easing 4 :movement [40 40] :start 9/8 :end 10/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [200 -200] :start 10/8 :end 11/8})
(partial func-movement/move {:easing 4 :movement [-100 100] :start 10/8 :end 11/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [-200 -200] :start 11/8 :end 12/8})
(partial func-movement/move {:easing 4 :movement [100 100] :start 11/8 :end 12/8})
(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 1 :fade-out-start 7/8 :fade-out-end 12/8 :fade-out-easing 6})]}})

(defn create-lyrics
  "Generates lyrics partial to the lyric-metadata, skipping manual coding."
  [line-metadata]
  (reduce (fn [acc elm]
    (into acc (lyrics/create-lyrics (get elm 1))))
      [] line-metadata))

(defn main
  []
  (create-lyrics lines)
)
