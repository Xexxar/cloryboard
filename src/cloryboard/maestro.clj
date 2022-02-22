(ns
  cloryboard.maestro
  (:require
   [cloryboard.maestro-generator :as maestro][cloryboard.events.0_beginning :as maestro-0][cloryboard.events.1_opening :as maestro-1][cloryboard.events.2_verse1 :as maestro-2][cloryboard.events.3_verse2 :as maestro-3][cloryboard.events.4_chorus1 :as maestro-4]))
 (defn
  main
  []
  (maestro/flatten-maestro
   [(maestro/flatten-maestro (maestro-0/main))
    (maestro/flatten-maestro (maestro-1/main))
    (maestro/flatten-maestro (maestro-2/main))
    (maestro/flatten-maestro (maestro-3/main))
    (maestro/flatten-maestro (maestro-4/main))]))