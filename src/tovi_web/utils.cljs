(ns tovi-web.utils)

(defn get-quantity [percentage]
  (let [percentage (/ percentage 100)]
    (* 1000 percentage)))

;; From this {1 {:id 1 :label {:value "Label" :error nil} :percentage {:value 100 :error nil} :quantity {:value 1000 ...} :unit "gr"} ...} 
;; To this [[1 {:id 1 :label "label" :percentage 100 :quantity 1000 :unit "gr"}] ...]
(defn format-ingredients [ingredients]
  (let [reduce-fun (fn [acc [k v]]
                     (conj
                      acc
                      [k (-> v
                             (update-in [:id] :value)
                             (update-in [:label] :value)
                             (update-in [:percentage] :value))]))]
    (reduce reduce-fun [] ingredients)))

;; From this {:id 1 :name "name" :desc "desc"}
;; To this {:id {:value 1} 
;;          :name {:value "name"} 
;;          :desc {:value" desc"}} 
;; (given certain keys)
(defn to-form-map [input keys]
  (reduce
   (fn [acc key]
     (update-in acc [key] #(hash-map :value %1)))
   input
   keys))

;; From this {:id {:value 1} :name {:value "name"} :desc {:value"desc"}} 
;; To this {:id 1 :name "name" :desc "desc" 
;; (given certain keys)
(defn from-form-map [input keys]
  (reduce
   (fn [acc key]
     (update-in acc [key] :value))
   input
   keys))