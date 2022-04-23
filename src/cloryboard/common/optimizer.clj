(ns cloryboard.common.optimizer
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.common.easings :as easings]
            [cloryboard.common.maths :as maths]
            [clojure.java.io :as io]))

(defn ramer-douglas-peucker-2D-magic
  "Line count optimizer based on the ramer douglas peucker algorithm, written
  with 2 dimensionality in mind. Used for M optimization. Also uses a dynamic
  epsilon based on time."
  )


(defn optimize-effect
  [effect]
  );;TODO lazy rn. optimization doesnt matter (cope)
