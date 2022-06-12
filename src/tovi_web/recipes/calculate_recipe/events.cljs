(ns tovi-web.recipes.calculate-recipe.events
  (:require [re-frame.core :refer [reg-event-db]]
            [tovi-web.utils :as utils]))

(reg-event-db
 ::balance-recipe
 (fn [db [_ recipe-id ingredient-id new-value]]
   (let [quantity (get-in db [:recipes recipe-id :ingredients ingredient-id :quantity])
         ingredients-id-to-update (remove #{ingredient-id}
                                          (-> db (get-in [:forms :recipe :ingredients]) keys))
         factor (/ new-value quantity)
         reduce-fn (fn [acc id]
                     (let [quantity (get-in db [:recipes recipe-id :ingredients id :quantity])]
                       (update-in
                        acc
                        [:forms :recipe :ingredients id :quantity :value]
                        #(js/parseInt (* quantity factor)))))]

     (reduce reduce-fn db ingredients-id-to-update))))

(reg-event-db
 ::calculate-recipe-dough-weight
 (fn [db _]
   (let [ingredients (get-in db [:forms :recipe :ingredients])
         dough-weight (reduce-kv
                       (fn [acc _ {:keys [quantity]}]
                         (+ acc (:value quantity)))
                       0
                       ingredients)]
     (assoc-in db [:forms :recipe :dough-weight :value] dough-weight))))

(defn total-recipe-percentage [ingredients]
  (reduce-kv
   (fn [acc _ {:keys [percentage]}]
     (+ acc (:value percentage)))
   0
   ingredients))

(reg-event-db
 ::balance-recipe-by-dough-weigth
 (fn [db [_ total-weigth]]
   (let [ingredients (get-in db [:forms :recipe :ingredients])
         total-percentage (total-recipe-percentage ingredients)
         reduce-fn (fn [acc k {:keys [percentage]}]
                     (update-in
                      acc
                      [:forms :recipe :ingredients k :quantity :value]
                      #(-> (utils/rule-of-three total-weigth total-percentage (:value percentage))
                           utils/to-int)))]
     (reduce-kv reduce-fn db ingredients))))