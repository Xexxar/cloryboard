(ns cloryboard.events.5-verse3
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.effects.lyrics :as lyrics]
            [cloryboard.common.effects :as effects]
            [cloryboard.functions.common :as func-common]
            [cloryboard.functions.move :as func-movement]
            [cloryboard.functions.rotate :as func-rotate]
            [cloryboard.functions.fade :as func-fade]
            [cloryboard.functions.restrictions :as restrict]
            [cloryboard.effects.particles :as particles]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(defn main
  []
  [])
