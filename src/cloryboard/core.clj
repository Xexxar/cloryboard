(ns cloryboard.core
  (:require [cloryboard.maestro-generator :as maestro-gen]
            [cloryboard.maestro :as maestro]
            [clojure.edn :as edn]
            [cloryboard.common.outputter :as outputter]))

(def storyboard-output-location
  "/home/xexxar/.osustable/drive_c/osu/Songs/Theocracy_-_I_AM/Theocracy - I AM (I Must Decrease).osb")


;;TODO: If you add new event, you have to run this thing twice or else it wont add it to the maestro.
(defn -main
  "Entry point of SB builder that converts the maestro output to .osb"
  [& args]
  (let [_ (println "Cloryboard is building SB...")
        _ (println "Step 1/3: Building Maestro... (I'm broken and won't load a new version, if your event is missing, rerun lein main)")
        _ (maestro-gen/main)
        _ (println "... done!")
        _ (println "Step 2/3: Building edn storyboard...")
        storyboard (maestro/main)
        _ (println "... done!")
        _ (println "Step 3/3: Converting storyboard to .osb...")
        final-output (outputter/output-storyboard storyboard)
        _ (println "... done!")]
    (spit storyboard-output-location final-output)))
