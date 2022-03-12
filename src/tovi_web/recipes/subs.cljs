(ns tovi-web.recipes.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
 ::get-db
 (fn [db _]
   db))

(reg-sub
 ::recipes
 (fn [db]
   (:recipes db)))

(reg-sub
 ::dialog-recipe-id
 (fn [db [_ dialog]]
   (when (= dialog (-> db :active-dialog :name))
     (-> db :active-dialog :recipe-id))))

(reg-sub
 ::dialog-recipe
 (fn [[_ dialog]]
   [(subscribe [::dialog-recipe-id dialog])
    (subscribe [::recipes])])
 (fn [[id recipes]]
   (recipes id)))

;; Used by create-recipe
(reg-sub
 ::recipe-ingredients
 (fn [db _]
   (-> db :forms :recipe :values :ingredients)))

(reg-sub
 ::ingredients
 (fn [db _]
   (-> db :ingredients vals vec)))
