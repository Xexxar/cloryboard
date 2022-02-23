(ns cloryboard.events.4_chorus1
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.functions.common :as func-common]
            [cloryboard.functions.move :as func-movement]
            [cloryboard.effects.particles :as particles]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

       :time {:start 193381 :end 198041}

(def base-line-settings
  {:position [320 240]
   :time {:start 107053 :end 111853}
   :text-offsets {:h 15 :v 50}
   :align "Centre"
   :tether "Centre"
   :metadata {:m-easing 1/32}
   :scale 0.2})

(def lines
  (mapv #(merge base-line-settings %)
    [{:line "I am the Alpha and Omega,\nfirst and last, eternally"
      :scale 0.15
      :time {:start 193381 :end 198041}}
     {:line "You cannot see me"
      :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})]
      :time {:start 198041 :end 202701}}
     {:line "The resurrection and the life"
      :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})]

      :time {:start 202701 :end 207361}}
     {:line "the doorway, and the vine,"
      :functions [(partial func-common/fade-in-and-out {:fade-in-start 0 :fade-in-end 1/8 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})]

      :time {:start 207361 :end 211876}}
     {:line "I am"
      :functions [(partial func-common/fade-in-and-out {:fade-in-start -1/8 :fade-in-end 0 :fade-in-easing 0 :fade-out-start 7/8 :fade-out-end 9/8 :fade-out-easing 1})
(partial func-movement/move {:easing 18 :movement [0 -120] :start 0 :end 9/8})
(partial func-movement/move-random-percent-of-movement {:easing 0 :movement [0 -20] :start 0 :end 9/8})]
      :scale 0.3
      :time {:start 212021 :end 216681}}
     ; {:line "I am the Alpha and Omega,\nfirst and last, eternally"
     ;  :scale 0.15
     ;  :time {:start 193381 :end 198041}}
     ; {:line "I am the Alpha and Omega,\nfirst and last, eternally"
     ;  :scale 0.15
     ;  :time {:start 193381 :end 198041}}
     ; {:line "I am the Alpha and Omega,\nfirst and last, eternally"
     ;  :scale 0.15
     ;  :time {:start 193381 :end 198041}}
]))

(def particles
  (particles/create-effect
    {:particle-count 100 ;; number of particles initialized
     :particle-depth-range nil ;; range of depath for effect
     :particle-scale 0.25
     :file "sb/dot.png"
     :time {:start 193381 :end 198041}
    }))

(defn create-lyrics
  "Generates lyrics partial to the lyric-metadata, skipping manual coding."
  [line-metadata]
  (reduce (fn [acc elm]
    (into acc (lyrics/create-lyrics elm)))
      [] line-metadata))

(defn main
  []
  [(create-lyrics lines)
  particles]
)
