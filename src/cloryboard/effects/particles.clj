(ns cloryboard.effects.particles
  (:require [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [cloryboard.common.image :as image]
            [cloryboard.functions.common :as func-common]
            [cloryboard.common.resolver :as resolver]
            [cloryboard.common.easings :as easings]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D Color Font FontMetrics]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

; ;;NOTE Here is the explanation of this effect.
; ;; We want to create a feeling of volume with this particle effect. Meaning, I
; ;; don't want to just use a single layer and scale randomly, I want the density
; ;; to feel real. Meaning, you provide me a the following structure, and I lay
; ;; out the particles to match a realistic density. (more small particles, few
; ;; large ones).
;
; ;; For depth, we are assuming a
;
; ; {:particle-count 0 ;; number of particles initialized
; ;  :particle-depth-range [] ;; range of depth for effect
; ;  :particle-scale 0
; ;  :file ""
; ;  :time {:start 0 :end 1}
; ; }
; ;
; ; (def screen-coordinates
; ;   [[-160 800][0 480]])
;
;; Guess I can try random first?
(defn generate-particles
  [p-count scale-range coords time files]
  (sort-by 
  	#(get-in % [:functions 0 :arguments 0])
  (mapv
    (fn [particle]
      {:type "Sprite"
       :filepath (get files (int (rand (count files))))
       :metadata time
       :tether "Centre"
       :layer "Foreground"
       :position [(+ (* (- (get-in coords [1 0]) (get-in coords [0 0])) (rand 1)) (get-in coords [0 0]))
                  (+ (* (- (get-in coords [1 1]) (get-in coords [0 1])) (rand 1)) (get-in coords [0 1]))]
       :functions [{:function "S"
                    :start 0
                    :end 0
                    :easing 0
                    :arguments (let [scale (+ (get scale-range 0) (* (- (get scale-range 1) (get scale-range 0)) (Math/pow (rand 1) 2)))]
                              [scale scale])}]})
    (range p-count))))
;
; (defn generate-three-dimensional-particles
;   [particle-count particle-scale particle-scale-range particle-depth-range file time]
;   nil)


;; NOTE: coords [[-107 0] [747 480]] is full screen
(defn create-box-of-particles
  [parameters]
  (let [p-count (get parameters :count)
        scale-range (get parameters :scale-range)
        files (get parameters :files)
        time (get parameters :time)
        coords (get parameters :coords)]
      (generate-particles
        p-count
        scale-range
        coords
        time
        files)))

; ; (defn create-3d-box-of-particles
; ;   [p-count scale-range time files time]
; ;   (let [scale-function (Math/sqrt (rand ))]))


;; NOTE: coords [[-107 0] [747 480]] is full screen

(defn- is-in-box?
  "Is the particle off screen"
  [box pos]
  (cond
    (<= (get pos 0) (get-in box [0 0])) ;; off on left side
    		[(+ (get-in box [1 0]) -1) (get pos 1)]
    (<= (get pos 1) (get-in box [0 1])) ;; above box
    		[(get pos 0) (+ (get-in box [1 1]) -1)] 
    (<= (get-in box [1 0]) (get pos 0)) ;; off on right side
    		[(+ (get-in box [0 0]) 1) (get pos 1)]
    (<= (get-in box [1 1]) (get pos 1)) ;; below box
    		[(get pos 0) (+ (get-in box [0 1]) 1)]
      ))

(defn get-particle-scale
  [object]
  (get-in (filterv #(= "S" (get % :function)) (get object :functions)) [0 :arguments 0]))

(defn use-up-movement-non-linear
  "run me on a particle and i will generate new functions"
  [functions currpos move start duration movement-borders easing]
  (loop [f functions l currpos s start e 0 d duration]
    (let [curr-elapsed (easings/easing-to-value easing (/ (+ (- s start) e) duration))
          prev-elapsed (easings/easing-to-value easing (/ (- s start) duration))
          curpos [(+ (get l 0) (* (get move 0) (- curr-elapsed prev-elapsed))) (+ (get l 1) (* (get move 1) (- curr-elapsed prev-elapsed)))]]
  		(cond 
  		  (>= 0 d)
  		    (conj f 
  		      {:function "M"
  		       :arguments [(get l 0) (get l 1) (get curpos 0) (get curpos 1)]
  		       :easing 0
  		       :start s
  		       :end (+ s e)})
  		  (some? (is-in-box? movement-borders curpos))
  		  		(let [newpos (is-in-box? movement-borders curpos)]
  		  	 (recur 
  		  		  (conj f 
  		        {:function "M"
  		         :arguments [(get l 0) (get l 1) (get curpos 0) (get curpos 1)]
  		         :easing 0
  		         :start s
  		         :end (+ s e)})
  		  		  newpos
  		  		  (+ s e)
  		  		  0
  		  		  (- d 1)))
  		  :else 
  		  	 (recur 
  		  		  f
  		  		  l
  		  		  s
  		  		  (+ e 1)
  		  		  (- d 1))))))

(defn use-up-movement
  "run me on a particle and i will generate new functions"
  [functions currpos move start duration movement-borders]
  (loop [f functions c currpos l currpos s start e 0 d duration]

  		(cond 
  		  (>= 0 d)
  		    (conj f 
  		      {:function "M"
  		       :arguments [(get l 0) (get l 1) (get c 0) (get c 1)]
  		       :easing 0
  		       :start s
  		       :end (+ s e)})
  		  (some? (is-in-box? movement-borders c))
  		  		(let [newpos (is-in-box? movement-borders c)]
  		  	 (recur 
  		  		  (conj f 
  		        {:function "M"
  		         :arguments [(get l 0) (get l 1) (get c 0) (get c 1)]
  		         :easing 0
  		         :start s
  		         :end (+ s e)})
  		  		  newpos
  		  		  newpos
  		  		  (+ s e)
  		  		  0
  		  		  (- d 1)))
  		  :else 
  		  	 (recur 
  		  		  f
  		  		  [(+ (get c 0) (get move 0)) (+ (get c 1) (get move 1))]
  		  		  l
  		  		  s
  		  		  (+ e 1)
  		  		  (- d 1)))))

(defn sum-vector
		[v]
  (reduce 
    (fn [acc elm]
      [(+ (get acc 0) (get elm 0)) (+ (get acc 1) (get elm 1))])
    v))

(defn apply-movement-to-object
  [object movement movement-borders scale-range]
  (let [start (int (+ (get-in object [:metadata :start]) (* (get movement :start) (- (get-in object [:metadata :end]) (get-in object [:metadata :start])))))
    			 end (int (+ (get-in object [:metadata :start]) (* (get movement :end) (- (get-in object [:metadata :end]) (get-in object [:metadata :start])))))
    			 currpos (resolver/get-current-effect-value object start "M")
    		  scale (get-particle-scale object)
    		  speed-multiplier (/ scale (get scale-range 0))
    			 duration (- end start)
    		  move [(/ (* speed-multiplier (get-in movement [:arguments 0])) duration)
    		        (/ (* speed-multiplier (get-in movement [:arguments 1])) duration)]
    			 ]
    (assoc object :functions
      (use-up-movement (get object :functions) currpos move start duration movement-borders))))

(defn prep-non-linear-functions
  [object movement speed-multiplier functions]
  (mapv 
    	(fn [func]
    			{:arguments (let [move [(* speed-multiplier (get-in movement [:arguments 0]))
    		                         (* speed-multiplier (get-in movement [:arguments 1]))]
    				                 start (get-in functions [0 :arguments])]
    	                [(+ (get start 0) (get func 0)) (+ (get start 1) (get func 1))
    	                 (+ (get start 0) (get func 0) (get move 0)) (+ (get start 1) (get func 1) (get move 1))])
    			 :start (int (+ (get-in object [:metadata :start]) (* (get movement :start) (- (get-in object [:metadata :end]) (get-in object [:metadata :start])))))
    			 :function "M" 
    			 :end (int (+ (get-in object [:metadata :start]) (* (get movement :end) (- (get-in object [:metadata :end]) (get-in object [:metadata :start])))))
    			 :easing (get movement :easing)})
    	(let [funcs (mapv #(get % :arguments) functions)
    	moves
						(reduce
						  (fn [acc elm]
						    (if (empty? acc)
						      [[0 0]]
						      (let [prev (get funcs (dec elm))
						            curr (get funcs elm)]
						        (conj acc [(- (get curr 0) (get prev 2)) (- (get curr 1) (get prev 3))]))))
						  []
						  (range (count funcs)))]
						(mapv #(sum-vector (take (inc %) moves)) (range (count moves))))))

(defn handle-non-linear
  [objs currobjindex movement movement-borders scale-range]
  (let [object (get objs currobjindex)
  						start (int (+ (get-in object [:metadata :start]) (* (get movement :start) (- (get-in object [:metadata :end]) (get-in object [:metadata :start])))))
    			 end (int (+ (get-in object [:metadata :start]) (* (get movement :end) (- (get-in object [:metadata :end]) (get-in object [:metadata :start])))))
    			 currpos (resolver/get-current-effect-value object start "M")
    		  scale (get-particle-scale object)
    		  speed-multiplier (/ scale (get scale-range 0))
    			 duration (- end start)
    		  move [(* speed-multiplier (get-in movement [:arguments 0]))
    		        (* speed-multiplier (get-in movement [:arguments 1]))]
    			 functions (prep-non-linear-functions object movement speed-multiplier
                    (use-up-movement-non-linear [] currpos move start duration movement-borders (get movement :easing)))] ;; turn this into the start pos of each function using mapv.
    (let [pain (reduce
    		(fn [acc elm]
    				(conj acc 
    				  (assoc object :functions (conj (filterv #(not= (get % :function) "M") (get object :functions)) elm))))
      (assoc-in objs [currobjindex :functions] (conj (get object :functions) (first functions)))
      (rest functions))]
    (mapv 
    		#(cond 
    		  (< % currobjindex)
    		    (get pain %)
    		  (= % (count pain))
    		    (if (< currobjindex %)
    		      (assoc-in (get pain %) [:functions 0 :start] (get movement :start))
    		      (get pain %))
    		  (= % currobjindex)
    		    (if (< % (count pain))
    		      (assoc-in (get pain %) [:functions 0 :end] (get movement :end))
    		      (get pain %))
    		  :else 
    		  	 (-> (get pain %)
    		  	   (assoc-in [:functions 0 :end] (get movement :end))
    		  	   (assoc-in [:functions 0 :start] (get movement :start))))
      (range (count pain))))))

(defn create-particle-routine
	 "literal hitler function that creates a box of particles and a vector of movement descripters and makes hte particles do the do."
  [parameters]
  (let [p-count (get parameters :count)
        scale-range (get parameters :scale-range)
        files (get parameters :files)
        time (get parameters :time)
        movements (get parameters :movements)
        max-width (* (get scale-range 1) (image/get-max-width files))
        movement-borders [[(- -107 max-width) (- 0 max-width)] [(+ 747 max-width) (+ 480 max-width)]]
      	 particles (mapv #(assoc % :metadata (merge (get % :metadata) {:bypasses #{"M"}}))
      	 									 (generate-particles
                    p-count
                    scale-range
                    movement-borders
                    time
                    files))]
    (reduce
    		(fn [acc object] ;; for each particle
    			 (into acc
    			 	 (reduce 
    			 	 		(fn [objs movement] ;; for each movement
    			 	 				(let [currobjindex (dec (count objs))]
    			 	 				  (if (= 0 (get movement :easing)) ;; check if this is a linear movement
    			 	 				    (assoc-in objs
    			 	 				    		[currobjindex]
    			 	 				    		  (apply-movement-to-object (get objs currobjindex) movement movement-borders scale-range))
    			 	 				    (handle-non-linear objs currobjindex movement movement-borders scale-range))))
    			 	   [object]
    			 	   movements)))
    		[]
    		particles)))
