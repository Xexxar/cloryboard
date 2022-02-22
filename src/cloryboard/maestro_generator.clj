(ns cloryboard.maestro-generator
  (:require [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [clojure.string :as str])
  (:import java.io.File))

(defn flatten-maestro
  "Simple flatten checker that flattens a nested [[] []] to -> [] or leaves [] alone."
  [sb-objects]
  (if (vector? (get sb-objects 0))
    (reduce into sb-objects)
    sb-objects))

(defn- generate-maestro-main
  [count abbrv]
  `(~'defn ~'main
    []
    (~'maestro/flatten-maestro ~(mapv (fn [index]
      `(~'maestro/flatten-maestro (~(symbol (str abbrv index "/main")))))
      (range count)))))

(defn- generate-maestro-namespace
  [namespaces abbrv]
  (let [require-string (symbol (reduce str "[cloryboard.maestro-generator :as maestro]"
                          (mapv (fn [index]
                            [(symbol (get namespaces index)) :as (symbol (str abbrv index))])
                            (range (count namespaces)))))]
  `(~'ns ~'cloryboard.maestro
    (:require ~require-string))))

(defn- generate-maestro-for-events
  [directory]
  (let [files (rest (map #(.getPath %) (file-seq directory)))
        namespaces (vec (sort (mapv
                      #(subs % (inc (str/index-of % " ")) (str/index-of % "\n"))
                      (map slurp files))))
        _ (pp/pprint namespaces)
        abbrv "maestro-"
        namespace-call (generate-maestro-namespace namespaces abbrv)
        maestro-main (generate-maestro-main (count namespaces) abbrv)
        out-string (with-out-str (pp/pprint [namespace-call maestro-main]))]
    (subs out-string 1 (- (count out-string) 2))))

(defn main
  "I generate a maestro that calls off to the main functions of all
   cloryboard/events/ and flattens them to a structure ready for the outputter
   to convert into .osb"
  []
  (spit "src/cloryboard/maestro.clj"
    (generate-maestro-for-events (clojure.java.io/file "src/cloryboard/events"))))
