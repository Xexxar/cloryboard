(ns cloryboard.common.image
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(defn load-image
  ([path]
  (load-image path ""))
  ([path name]
  (let [img (ImageIO/read (io/file path))]
  {:name name
   :path path
   :width (.getWidth img)
   :height (.getHeight img)})))

(defn get-max-width
	 [files]
	 (reduce 
	   (fn [acc elm]
	 			 (max acc (* (Math/sqrt 2) (max (get elm :width) (get elm :height)))))
	   0 (mapv #(load-image %) files)))