(ns
  cloryboard.maestro
  (:require
   [cloryboard.maestro-generator :as maestro][cloryboard.events.0_beginning :as maestro-0][cloryboard.events.1_opening :as maestro-1][cloryboard.events.2_verse1 :as maestro-2][cloryboard.events.3_verse2 :as maestro-3][cloryboard.events.4_chorus1 :as maestro-4][cloryboard.events.5-verse3 :as maestro-5][cloryboard.events.6-chorus2 :as maestro-6][cloryboard.events.7-bridge1 :as maestro-7][cloryboard.events.8-bridge2 :as maestro-8][cloryboard.events.9-bridge3 :as maestro-9][cloryboard.events.a-1-solo :as maestro-10][cloryboard.events.a-2-verse :as maestro-11][cloryboard.events.a-3-ending :as maestro-12]))
 (defn
  main
  []
  (maestro/flatten-maestro
   [(maestro/flatten-maestro (maestro-0/main))
    (maestro/flatten-maestro (maestro-1/main))
    (maestro/flatten-maestro (maestro-2/main))
    (maestro/flatten-maestro (maestro-3/main))
    (maestro/flatten-maestro (maestro-4/main))
    (maestro/flatten-maestro (maestro-5/main))
    (maestro/flatten-maestro (maestro-6/main))
    (maestro/flatten-maestro (maestro-7/main))
    (maestro/flatten-maestro (maestro-8/main))
    (maestro/flatten-maestro (maestro-9/main))
    (maestro/flatten-maestro (maestro-10/main))
    (maestro/flatten-maestro (maestro-11/main))
    (maestro/flatten-maestro (maestro-12/main))]))