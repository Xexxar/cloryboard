(ns cloryboard.effects.lyrics
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

(def letters-to-index
  (let [letters "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_\"'-,.?:;!&1234567890 "
        c (vec (sort (mapv char "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_\"'-,.?:;!&1234567890 ")))]
    (reduce merge (mapv (fn [k] {(get c k) k}) (range (count c))))))

(def letter-data
  (let [path "sb/lyrics/"]
    (merge {\newline {:name \newline :height 148 :width 0}}
    (reduce merge (mapv
      (fn [elm]
        {(get elm 0)
          (image/load-image (str path (get elm 1) ".png") (get elm 0))})
      letters-to-index)))))

(defn align-line
  [objects align]
  (let [lines (group-by #(get-in % [:position 1]) objects)
        max-width (reduce (fn [acc k] (if (< acc (get-in k [:position 0])) (get-in k [:position 0]) acc)) 0 objects)]
  (cond
    (= align "Left")
      objects ;; its created this way, so nothing is needed.
    (= align "Right")
      (reduce into (mapv
        (fn [line-pre]
          (let [line (get line-pre 1)
                line-width (get-in (last line) [:position 0])
                line-shift (double (- max-width line-width))]
            (mapv #(update-in % [:position 0] (fn [x] (+ x line-shift))) line)))
        lines))
    :else ;Centre
      (reduce into (mapv
        (fn [line-pre]
          (let [line (get line-pre 1)
                line-width (get-in (last line) [:position 0])
                line-shift (double (/ (- max-width line-width) 2))]
            (mapv #(update-in % [:position 0] (fn [x] (+ x line-shift))) line)))
        lines)))))

(defn recursively-build-line
  [acc line-data pos index h-offset v-offset time]
  (if (nil? (get line-data index))
    acc
    (let [pos-delta (cond
                      (= index 0)
                        [0 0]
                      (= \newline (get-in line-data [index :name]))
                        [0 (+ (get pos 1) v-offset (get-in line-data [index :height]))]
                      (= \newline (get-in line-data [(dec index) :name]))
                        [0 (get pos 1)]
                      :else
                        [(double (+ (get pos 0) h-offset
                          (/ (get-in line-data [(dec index) :width]) 2)
                          (/ (get-in line-data [index :width]) 2)))
                         (get pos 1)])]
    (recursively-build-line
      (if (contains? #{\space \newline} (get-in line-data [index :name]))
        acc
        (conj acc {:type "Sprite"
                   :filepath (get-in line-data [index :path])
                   :metadata time
                   :tether "Centre"
                   :functions []
                   :layer "Foreground"
                   :position pos-delta}))
      line-data
      pos-delta
      (inc index)
      h-offset
      v-offset
      time))))

(defn build-line
  [line h-offset v-offset time]
  (recursively-build-line [] (mapv #(get letter-data %) line) 0 0 h-offset v-offset time))

(defn initialize-lyrics
  [metadata]
  (let [line (get metadata :line)
        tether (get metadata :tether)
        position (get metadata :position)
        align (get metadata :align)
        scale (get metadata :scale)
        h-offset (get-in metadata [:text-offsets :h])
        v-offset (get-in metadata [:text-offsets :v])
        time (get metadata :time)]
    (-> (build-line line h-offset v-offset time)
        (align-line align)
        (func-common/scale-effect scale)
        (func-common/position-effect tether position))))

(defn create-lyrics
  [metadata]
  (resolver/resolve-function-timing
    (resolver/apply-functions-to-objects
      (initialize-lyrics metadata)
      (get metadata :functions)
      (get metadata :metadata))))
