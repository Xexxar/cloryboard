(ns cloryboard.common.easings)

(defn- reverse
  [function value]
  (- 1 (function (- 1 value))))

(defn- to-in-out
  [function value]
  (* 0.5
    (if (< value 0.5)
      (function (* 2 value))
      (- 2 (function (- 2 (* 2 value)))))))

(defn- linear
  [x]
  x)

(defn- quad
  [x]
  (* x x))

(defn- cubic
  [x]
  (* x x x))

(defn- quart
  [x]
  (* x x x x))

(defn- quint
  [x]
  (* x x x x x))

(defn- sine
  [x]
  (- 1 (Math/cos (* x (/ Math/PI 2)))))

(defn- expo
  [x]
  (Math/pow 2 (* 10 (- x 1))))

(defn- circ
  [x]
  (- 1 (Math/sqrt (- 1 (* x x)))));

(defn- elastic-out
  [percent x]
  (+ 1 (* (Math/pow 2 (* -10 x)) (Math/sin (* (- (* x percent) 0.075) (/ (* 2 Math/PI) 0.3))))))

(defn- back-in
  [x]
  (* x x (- (* (+ 1.70158 1) x) 1.70158)))

(defn- back-in-out
  [x]
  (* x x (- (* (+ (* 1.525 1.70158) 1) x) (* 1.525 1.70158))))

(defn- bounce-out
  [x]
  (cond
    (< x (/ 1 2.75))
      (* 7.5625 x x)
    (< x (/ 2 2.75))
      (+ (* 7.5625 (- x (/ 1.5 2.75)) (- x (/ 1.5 2.75))) 0.75)
    (< x (/ 2.5 2.75))
      (+ (* 7.5625 (- x (/ 2.25 2.75)) (- x (/ 2.25 2.75))) 0.9375)
    :else
      (+ (* 7.5625 (- x (/ 2.625 2.75)) (- x (/ 2.625 2.75))) 0.984375)))

(def easing-names
  {"Linear" :0
   "InQuad" :3
   "OutQuad" :4
   "InOutQuad" :5
   "InCubic" :6
   "OutCubic" :7
   "InOutCubic" :8
   "InQuart" :9
   "OutQuart" :10
   "InOutQuart" :11
   "InQuint" :12
   "OutQuint" :13
   "InOutQuint" :14
   "InSine" :15
   "OutSine" :16
   "InOutSine" :17
   "InExpo" :18
   "OutExpo" :19
   "InOutExpo" :20
   "InCirc" :21
   "OutCirc" :22
   "InOutCirc" :23
   "InElastic" :24
   "OutElastic" :25
   "OutElasticHalf" :26
   "OutElasticQuarter" :27
   "InOutElastic" :28
   "InBack" :29
   "OutBack" :30 ;; g'day mate!
   "InOutBack" :31
   "InBounce" :32
   "OutBounce" :33
   "InOutBounce" :34})

(def easings
  {0 (fn [x] (linear x))
   1 (fn [x] (identity 0)) ;;TODO i dont really know what the function for these is.
   2 (fn [x] (identity 0)) ;;TODO i dont really know what the function for these is.
   3 (fn [x] (quad x))
   4 (fn [x] (reverse quad x))
   5 (fn [x] (to-in-out quad x))
   6 (fn [x] (cubic x))
   7 (fn [x] (reverse cubic x))
   8 (fn [x] (to-in-out cubic x))
   9 (fn [x] (quart x))
   10 (fn [x] (reverse quart x))
   11 (fn [x] (to-in-out quart x))
   12 (fn [x] (quint x))
   13 (fn [x] (reverse quint x))
   14 (fn [x] (to-in-out quint x))
   15 (fn [x] (sine x))
   16 (fn [x] (reverse sine x))
   17 (fn [x] (to-in-out sine x))
   18 (fn [x] (expo x))
   19 (fn [x] (reverse expo x))
   20 (fn [x] (to-in-out expo x))
   21 (fn [x] (circ x))
   22 (fn [x] (reverse circ x))
   23 (fn [x] (to-in-out circ x))
   24 (fn [x] (reverse elastic-out x))
   25 (fn [x] (elastic-out 1.0 x))
   26 (fn [x] (elastic-out 0.5 x))
   27 (fn [x] (elastic-out 0.25 x))
   28 (fn [x] (to-in-out #(reverse (partial elastic-out 1.0) %) x))
   29 (fn [x] (back-in x))
   30 (fn [x] (reverse back-in x))
   31 (fn [x] (to-in-out back-in-out x))
   32 (fn [x] (reverse bounce-out x))
   33 (fn [x] (bounce-out x))
   34 (fn [x] (to-in-out #(reverse bounce-out %) x))})

(defn easing-to-value
  [easing value]
  (let [easing-function (fn [e v] (if (contains? easings e)
                                        ((get easings e) v)
                                        (throw (Exception. (str "Invalid Easing: " e)))))]
    (cond
      (string? easing)
        (easing-function (get easing-names easing) value)
      (number? easing)
        (easing-function easing value)
      :else
        (throw (Exception. (str "Invalid Easing: " easing))))))
