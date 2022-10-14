(ns cloryboard.common.outputter
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as str]))

; (defn convert-loop-to-output
;   []
;   (let [function (get functions index)]
;     (if (nil? function)
;       acc
;       (convert-function-to-output functions (inc index)
;         (str acc " "
;           (str/join "," (reduce conj
;                           [(get function :function)
;                            (get function :easing)
;                            (get function :start)
;                            (get function :end)]
;                           (get function :arguments)))
;         "\n"))))))

(defn convert-function-to-output
  [functions index acc]
  (let [function (get functions index)]
    (if (nil? function)
      acc
      ; (if (= (get function :function) "L") ;;TODO I'm not ready to handle L or T functions!

      (convert-function-to-output functions (inc index)
        (str acc " "
          (str/join "," (reduce conj
                          [(get function :function)
                           (get function :easing)
                           (get function :start)
                           (get function :end)]
                          (get function :arguments)))
        "\n")))))

(defn convert-object-to-output
  [layer object]
  (str 
  		(str/join "," [(get object :type)
                         (get object :layer)
                         (get object :tether)
                         (get object :filepath)
                         (get-in object [:position 0])
                         (get-in object [:position 1])])
        "\n"
        (convert-function-to-output (get object :functions) 0 ""
        )))

(defn convert-layer-to-output
  [layers index]
  (let [layer (get layers index)]
    (if (nil? layer)
      nil
      (reduce
      	(fn [acc elm]
      		(str acc
      				(convert-object-to-output layer elm)))
      	"" layer))))

(defn output-storyboard
  "Functionally this is the main method to convert a raw .edn SB to .osb"
  [storyboard]
  (let [groups {"Video" "//Background and Video events\n"
                "Background" "//Storyboard Layer 0 (Background)\n"
                "Fail" "//Storyboard Layer 1 (Fail)\n"
                "Pass" "//Storyboard Layer 2 (Pass)\n"
                "Foreground" "//Storyboard Layer 3 (Foreground)\n"
                "Overlay" "//Storyboard Layer 4 (Overlay)\n"
                "Sound" "//Storyboard Sound Samples\n"}
        layers (group-by #(get % :layer) storyboard)]
    (-> "[Events]\n"
      (str (get groups "Video"))
      (str (convert-layer-to-output layers "Video"))
      (str (get groups "Background"))
      (str (convert-layer-to-output layers "Background"))
      (str (get groups "Fail"))
      (str (convert-layer-to-output layers "Fail"))
      (str (get groups "Pass"))
      (str (convert-layer-to-output layers "Pass"))
      (str (get groups "Foreground"))
      (str (convert-layer-to-output layers "Foreground"))
      (str (get groups "Overlay"))
      (str (convert-layer-to-output layers "Overlay"))
      (str (get groups "Sound"))
      (str (convert-layer-to-output layers "Sound")))))
