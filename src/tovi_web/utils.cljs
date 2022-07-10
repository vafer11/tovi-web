(ns tovi-web.utils
  (:require [clojure.string :as st] 
            [malli.core :as m] 
            [malli.error :as me]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(defn text-field [min max]
  [:fn {:error/fn (fn [{:keys [value]} _]
                    (cond
                      (< (count value) min) (str "Should contain at least " min " characters")
                      (> (count value) max) (str "Should contain less than " max " characters")))}
   (fn [x] (and (string? x) (>= (count x) min)  (<= (count x) max)))])

(defn to-int [v]
  (let [v (js/parseInt v)]
    (if (js/isNaN v) 0 v)))

(defn rule-of-three [a b c] 
  (/ (* c a) b))

(defn get-quantity [percentage]
  (let [percentage (/ percentage 100)]
    (* 1000 percentage)))

(defn- remove-blank
  "Dissoc from map keywords which value is a blank string"
  [data]
  (reduce-kv
   #(if (and (string? %3) (st/blank? %3)) (dissoc %1 %2) %1)
   data
   data))

(defn validate-schema [schema form]
  (-> schema
      (m/explain (remove-blank form))
      (me/humanize
       {:errors (-> me/default-errors
                    (assoc ::m/missing-key
                           {:error/fn
                            (fn [_ _]
                              (str "This field is required"))}))})))
