(ns cloryboard.generator.lyrics
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            ; [clisk.effects :as effect]
            ; [mikera.image.colours :as colour]
            [clojure.set :as set]
            [clojure.string :as str])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [com.jhlabs.image GaussianFilter]
           [java.io File])
  (:use [clisk.live]))

(defn generate-blur-sub-image
  [image-src]
  (let [width (.getWidth image-src)])
  )

(defn str->img [string filename]
  (let [file (File. (str "./resources/generated/" filename ".png"))
        font-size 120
        g (.createGraphics (BufferedImage. 1 1 BufferedImage/TYPE_INT_ARGB))
        font (Font. "Kelmscott" Font/PLAIN font-size),
        fontmetrics (.getFontMetrics g font)
        width (.stringWidth fontmetrics string)
        height 148
        image (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
        graphics (.createGraphics image)
        sub-glow ]
    (.setColor graphics Color/WHITE)
    (.setFont graphics font)
    (.drawString graphics string 0 110)
    ; (show image)
    (ImageIO/write image "png" file)))

(defn generate-lyrics-images
  [letters]
  (let [directory "./resources/generated/"]
    (mapv #(str->img (str (get % 0)) (str (get % 1))) letters)))


;; Repl to run to generate image set.
; (generate-lyrics-images
; (let [letters "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_\"'-,.?:;!&1234567890 "
;       c (vec (sort (mapv char letters)))]
;   (reduce merge (mapv (fn [k] {(get c k) k}) (range (count c))))))
