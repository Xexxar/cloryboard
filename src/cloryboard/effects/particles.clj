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

;;NOTE Here is the explanation of this effect.
;; We want to create a feeling of volume with this particle effect. Meaning, I
;; don't want to just use a single layer and scale randomly, I want the density
;; to feel real. Meaning, you provide me a the following structure, and I lay
;; out the particles to match a realistic density. (more small particles, few
;; large ones).

;; For depth, we are assuming a

; {:particle-count 0 ;; number of particles initialized
;  :particle-depth-range [] ;; range of depth for effect
;  :particle-scale 0
;  :file ""
;  :time {:start 0 :end 1}
; }
;
; (def screen-coordinates
;   [[-160 800][0 480]])

;; Guess I can try random first?
(defn generate-two-dimensional-particles
  [particle-count particle-scale particle-scale-range file time]
  (mapv
    (fn [particle]
      {:type "Sprite"
       :filepath file
       :metadata time
       :tether "Centre"
       :layer "Foreground"
       :position [(- (* 854 (rand 1)) 107) (* 480 (rand 1))]
       :functions [{:function "S"
                    :start 0
                    :end 1
                    :easing 0
                    :arguments [particle-scale]}]})
    (range particle-count)))

(defn generate-three-dimensional-particles
  [particle-count particle-scale particle-scale-range particle-depth-range file time]
  nil)

(defn initialize-effect
  [metadata]
  (let [particle-count (get metadata :particle-count)
        particle-scale (get metadata :particle-scale)
        particle-scale-range (get metadata :particle-scale-range)
        particle-depth-range (get metadata :particle-depth-range)
        file (get metadata :file)
        time (get metadata :time)
        three-dimensional? (some? :particle-depth-range)]
    ; (if three-dimensional?
    ;   ; (generate-three-dimensional-particles
    ;   ;   particle-count
    ;   ;   particle-scale
    ;   ;   particle-scale-range
    ;   ;   particle-depth-range
    ;   ;   file
    ;   ;   time)
      (generate-two-dimensional-particles
        particle-count
        particle-scale
        particle-scale-range
        file
        time)))

(defn create-effect
  [metadata]
  (resolver/resolve-function-timing
    (resolver/apply-functions-to-objects
      (initialize-effect metadata)
      (get metadata :functions)
      (get metadata :metadata))))
