(ns cloryboard.common.optimizer
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.common.easings :as easings]
            [cloryboard.common.maths :as maths]
            [clojure.java.io :as io]))

(defn ramer-douglas-peucker-1D-magic
  "Line count optimizer based on the ramer douglas peucker algorithm, written
  with 1 dimensionality in mind. Used for M optimization. Also uses a dynamic
  epsilon based on time."
  [functions epsilon]
  (let [values (mapv #(get-in :arguments))]
    (reduce
      (fn [acc elm]
        (conj acc
          ()))
       []
       functions))

(defn ramer-douglas-peucker-2D-magic
  "Line count optimizer based on the ramer douglas peucker algorithm, written
  with 2 dimensionality in mind. Used for M optimization. Also uses a dynamic
  epsilon based on time."
  [functions]
  (reduce
    (fn [acc elm]
      )))


(defn optimize-effect
  [objects]
  (mapv
    (fn )))
