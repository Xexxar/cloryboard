(ns cloryboard.common.effects
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.common.resolver :as resolver]))

;; Universal structure for effects?
; {:effect
;  :effect-parameters
;  :functions
;  :metadata}


(defn create-effect
  [effect]
  (resolver/resolve-function-timing
    (resolver/apply-functions-to-objects
      ((get effect :effect) (get effect :effect-parameters))
      (get effect :functions)
      (get effect :metadata))))

(defn create-effects
  [effects]
  (reduce (fn [acc elm]
    (into acc (create-effect elm)))
    []
    effects))
