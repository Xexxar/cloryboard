(ns cloryboard.effects.particles
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [cloryboard.common.image :as image]
            [cloryboard.functions.common :as func-common]
            [cloryboard.common.resolver :as resolver]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

; ;;NOTE Here is the explanation of this effect.
; ;; We want to create a feeling of volume with this particle effect. Meaning, I
; ;; don't want to just use a single layer and scale randomly, I want the density
; ;; to feel real. Meaning, you provide me a the following structure, and I lay
; ;; out the particles to match a realistic density. (more small particles, few
; ;; large ones).
;
; ;; For depth, we are assuming a
;
; ; {:particle-count 0 ;; number of particles initialized
; ;  :particle-depth-range [] ;; range of depth for effect
; ;  :particle-scale 0
; ;  :file ""
; ;  :time {:start 0 :end 1}
; ; }
; ;
; ; (def screen-coordinates
; ;   [[-160 800][0 480]])
;
;; Guess I can try random first?
(defn generate-particles
  [p-count scale-range coords time files time]
  (mapv
    (fn [particle]
      {:type "Sprite"
       :filepath (get files (int (rand (count files))))
       :metadata time
       :tether "Centre"
       :layer "Foreground"
       :position [(+ (* (- (get-in coords [1 0]) (get-in coords [0 0])) (rand 1)) (get-in coords [0 0]))
                  (+ (* (- (get-in coords [1 1]) (get-in coords [0 1])) (rand 1)) (get-in coords [0 1]))]
       :functions [{:function "S"
                    :start 0
                    :end 1
                    :easing 0
                    :arguments (let [scale (+ (get scale-range 0) (* (- (get scale-range 1) (get scale-range 0)) (rand 1)))]
                              [scale scale])}]})
    (range p-count)))
;
; (defn generate-three-dimensional-particles
;   [particle-count particle-scale particle-scale-range particle-depth-range file time]
;   nil)


;; NOTE: coords [[-107 0] [747 480]] is full screen
(defn create-box-of-particles
  [parameters]
  (let [p-count (get parameters :count)
        scale-range (get parameters :scale-range)
        files (get parameters :files)
        time (get parameters :time)
        coords (get parameters :coords)]
      (generate-particles
        p-count
        scale-range
        coords
        time
        files
        time)))
