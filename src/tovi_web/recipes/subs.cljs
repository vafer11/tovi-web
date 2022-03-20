(ns tovi-web.recipes.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
 ::recipes
 (fn [db]
   (:recipes db)))

; Used by view-recipe, calculate-recipe, and delete-recipe dialog.
(reg-sub
 ::active-dialog-recipe-id
 (fn [db [_ dialog]]
   (get-in db [:active-dialog dialog :active-recipe-id])))

; Used by view-recipe and calculate recipe dialog.
(reg-sub
 ::active-dialog-recipe
 (fn [[_ dialog]]
   [(subscribe [::active-dialog-recipe-id dialog])
    (subscribe [::recipes])])
 (fn [[id recipes]]
   (recipes id)))


(reg-sub
 ::get-db
 (fn [db _]
   db))


;; Used by recipe component 
(reg-sub
 ::form-recipe-ingredients
 (fn [db _]
   (-> db :forms :recipe :values :ingredients vec)))

(reg-sub
 ::ingredients
 (fn [db _]
   (-> db :ingredients vals vec)))

;; Calculate recipe dialog subscriptions
(reg-sub
 ::recipe-total-dough-weight
 (fn [db [_ id]]
   (let [ingredients (get-in db [:recipes id :ingredients])]
     (reduce-kv
      (fn [acc _ {:keys [quantity]}] (+ acc quantity))
      0
      ingredients))))
