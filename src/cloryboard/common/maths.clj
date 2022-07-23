(ns cloryboard.common.maths
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(defn sum-vars-ignore-nil
  [& args]
  (reduce + (filter #(some? %) args)))

(defn time-to-ms
  [input]
  (if (string? input)
    (let [trimmed (if (some? (str/index-of input " "))
                        (subs input 0 (str/index-of input " "))
                        input)
          time (str/split trimmed #":")
          min (edn/read-string (get time 0))
          sec (edn/read-string (get time 1))
          mil (edn/read-string (get time 2))]
      (+ (* 60000 min) (* 1000 sec) mil))))

(defn vec-multiply
  [scaler vector]
  (mapv #(* scaler %) vector))

(defn vec-multiply-vectors
  [vector v-scalers]
  (mapv #(* (get vector %) (get v-scalers %)) (range (dec (count vector)))))

(defn vec-subtract
  [vec1 vec2]
  (mapv
    #(- (get vec1 %) (get vec2 %))
    (range (count vec1))))

(defn vec-add
  [vec1 vec2]
  (mapv
    #(+ (get vec1 %) (get vec2 %))
    (range (count vec1))))

(defn get-center-position
  [objects]
  (let [max-height (reduce (fn [acc k] (if (< acc (get-in k [:position 1])) (get-in k [:position 1]) acc)) -1000000 objects)
        max-width (reduce (fn [acc k] (if (< acc (get-in k [:position 0])) (get-in k [:position 0]) acc)) -1000000 objects)
        min-height (reduce (fn [acc k] (if (> acc (get-in k [:position 1])) (get-in k [:position 1]) acc)) max-height objects)
        min-width (reduce (fn [acc k] (if (> acc (get-in k [:position 0])) (get-in k [:position 0]) acc)) max-width objects)]
    [(double (/ (+ max-width min-width) 2)) (double (/ (+ max-height min-height) 2))]))

(defn get-tether-position
  [objects tether]
  (cond
    (= tether "Centre")
      (get-center-position objects)
;; TODO Need to add the rest of the tether possibilities.
))
