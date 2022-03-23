(ns tovi-web.utils)

(defn get-quantity [percentage]
  (let [percentage (/ percentage 100)]
    (* 1000 percentage)))

;; From this {1 {:id 1 :label {:value "Label" :error nil} :percentage {:value 100 :error nil} :quantity {:value 1000 ...} :unit "gr"} ...} 
;; Return this [[1 {:id 1 :label "label" :percentage 100 :quantity 1000 :unit "gr"}] ...]
(defn format-ingredients [ingredients]
  (let [reduce-fun (fn [acc [k v]]
                     (conj
                      acc
                      [k (-> v
                             (update-in [:id] :value)
                             (update-in [:label] :value)
                             (update-in [:percentage] :value))]))]
    (reduce reduce-fun [] ingredients)))