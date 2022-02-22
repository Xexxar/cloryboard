(ns storyboard.tools-test
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.test :as t]
            [storyboard.tools :as ut]))

(t/deftest time-to-ms-test
  (t/testing "time-to-ms test"
    (t/is (= (ut/time-to-ms "01:46:673 - ") 106673))
    (t/is (= (ut/time-to-ms "01:46:673") 106673))
  )
)