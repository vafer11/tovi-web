(ns tovi-web.utils)

(defn to-int [v]
  (let [v (js/parseInt v)]
    (if (js/isNaN v) 0 v)))

(defn rule-of-three [a b c] 
  (/ (* c a) b))

(defn get-quantity [percentage]
  (let [percentage (/ percentage 100)]
    (* 1000 percentage)))