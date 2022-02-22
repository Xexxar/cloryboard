(ns storyboard.outputter-test
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.test :as t]
            [storyboard.outputter :as ut]))

(spit "target/test-out.edn" (ut/output-storyboard (edn/read-string (slurp "target/test.edn"))))

(def sample-storyboard
  [{:type "Sprite"
   :filepath "sb/background.png"
   :layer "Background"
   :tether "Centre"
   :position {:x 320 :y 240}
   :functions [{:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}]}
  {:type "Sprite"
   :filepath "sb/background.png"
   :layer "Background"
   :tether "Centre"
   :position {:x 320 :y 240}
   :functions [{:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}]}
  {:type "Sprite"
   :filepath "sb/background.png"
   :layer "Background"
   :tether "Centre"
   :position {:x 320 :y 240}
   :functions [{:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}]}
  {:type "Sprite"
   :filepath "sb/background.png"
   :layer "Background"
   :tether "Centre"
   :position {:x 320 :y 240}
   :functions [{:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}]}
  {:type "Sprite"
   :filepath "sb/background.png"
   :layer "Background"
   :tether "Centre"
   :position {:x 320 :y 240}
   :functions [{:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}]}
  {:type "Sprite"
   :filepath "sb/background.png"
   :layer "Foreground"
   :tether "Centre"
   :position {:x 320 :y 240}
   :functions [{:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}
               {:function "S"
                :easing "1"
                :start 12032
                :end 12390}]}])

(t/deftest does-it-output
  (t/testing "does-it-output"
    (t/is (some? (ut/output-storyboard sample-storyboard)))))
