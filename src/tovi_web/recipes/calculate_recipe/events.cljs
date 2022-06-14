(ns tovi-web.recipes.calculate-recipe.events
  (:require [re-frame.core :refer [reg-event-db]]
            [tovi-web.utils :as utils]))

(reg-event-db
 ::set-field-value
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values id] input)))

(reg-event-db
 ::set-quantity-value
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values :ingredients id :quantity] input)))

(reg-event-db
 ::balance-recipe
 (fn [db [_ recipe-id ingredient-id new-value]]
   (let [quantity (get-in db [:recipes recipe-id :ingredients ingredient-id :quantity])
         ingredients-id-to-update (remove #{ingredient-id}
                                          (-> db (get-in [:forms :recipe :values :ingredients]) keys))
         factor (/ new-value quantity)
         reduce-fn (fn [acc id]
                     (let [quantity (get-in db [:recipes recipe-id :ingredients id :quantity])]
                       (update-in
                        acc
                        [:forms :recipe :values :ingredients id :quantity]
                        #(js/parseInt (* quantity factor)))))]

     (reduce reduce-fn db ingredients-id-to-update))))

(reg-event-db
 ::calculate-recipe-dough-weight
 (fn [db _]
   (let [ingredients (get-in db [:forms :recipe :values :ingredients])
         dough-weight (reduce-kv
                       (fn [acc _ {:keys [quantity]}]
                         (+ acc quantity))
                       0
                       ingredients)]
     (assoc-in db [:forms :recipe :values :dough-weight] dough-weight))))

(defn total-recipe-percentage [ingredients]
  (reduce-kv
   (fn [acc _ {:keys [percentage]}]
     (+ acc percentage))
   0
   ingredients))

(reg-event-db
 ::balance-recipe-by-dough-weigth
 (fn [db [_ total-weigth]]
   (let [ingredients (get-in db [:forms :recipe :values :ingredients])
         total-percentage (total-recipe-percentage ingredients)
         reduce-fn (fn [acc k {:keys [percentage]}]
                     (update-in
                      acc
                      [:forms :recipe :values :ingredients k :quantity]
                      #(-> (utils/rule-of-three total-weigth total-percentage percentage)
                           utils/to-int)))]
     (reduce-kv reduce-fn db ingredients))))