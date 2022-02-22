(ns cloryboard.common.resolver_test
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]
            [cloryboard.common.easings :as easings]
[cloryboard.functions.movement :as func-movement]
            [cloryboard.common.maths :as maths]
            [cloryboard.common.resolver :as ut]
            [clojure.test :as t]
            [clojure.java.io :as io]))

(def movement-test-input
[{:filepath "sb/glow.png"
   :type "Sprite"
   :layer "Foreground"
   :tether "Centre"
   :position [320 240]
   :functions [
; {:function "S"
;                  :start 27853
;                  :end 30253
;                  :easing 0
;                  :arguments [75]}
;                 {:function "R"
;                  :start 27853
;                  :end 30253
;                  :easing 0
;                  :arguments [1.5707963267948966]}
;                 {:function "F"
;                  :start 27853
;                  :end 30253
;                  :easing 6
;                  :arguments [0 1]}
{:function "M"
 :start 0
 :end 1
 :easing 0
 :arguments [0 0 100 0]}
{:function "M"
 :start 0
 :end 1
 :easing 0
 :arguments [0 0 -100 0]}
; {:function "M"
;  :start 1/4
;  :end 3/4
;  :easing 6
;  :arguments [0 0 0 100]}
; {:function "M"
;  :start 7/8
;  :end 1
;  :easing 0
;  :arguments [0 0 0 1000]}
]}])

(t/deftest movement-function-resolver-test
  (t/testing "Grand Sovereign Mover."
    (let [out (ut/grand-sovereign-supreme-master-general-resolver {:m-easing 1/32} movement-test-input)])))

(pp/pprint (ut/grand-sovereign-supreme-master-general-resolver {:m-easing 1/16} movement-test-input))

(pp/pprint (ut/grand-sovereign-supreme-master-general-resolver {:m-easing 1/16} (edn/read-string (slurp "target/wtf.edn")))))


(pp/pprint
(ut/grand-sovereign-supreme-master-general-resolver {:m-easing 1/16}
(reduce (fn [acc func] (func acc))
  movement-test-input
  [(partial func-movement/move {:easing 0 :movement [0 -1000000] :start 0 :end 1})
   (partial func-movement/move {:easing 0 :movement [0 1000000] :start 0 :end 1})]
)))

(pp/pprint (ut/apply-functions-to-objects
    (edn/read-string (slurp "target/wtf.edn"))
    [(partial func-movement/move {:easing 0 :movement [0 -1000000] :start 0 :end 1})
     (partial func-movement/move {:easing 0 :movement [0 1000000] :start 0 :end 1})]
nil))
