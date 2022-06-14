(ns tovi-web.recipes.calculate-recipe.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::recipe-id
 (fn [db _]
   (-> db :forms :recipe :values :id)))

(reg-sub
 ::recipe-ingredients
 (fn [db _]
   (-> db :forms :recipe :values :ingredients)))

(reg-sub
 ::quantity-value
 (fn [db [_ id]]
   (get-in db [:forms :recipe :values :ingredients id :quantity])))

(reg-sub
 ::field-value
 (fn [db [_ id]]
   (get-in db [:forms :recipe :values id])))