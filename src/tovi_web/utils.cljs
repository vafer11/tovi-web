(ns tovi-web.utils)

(defn get-quantity [percentage]
  (let [percentage (/ percentage 100)]
    (* 1000 percentage)))