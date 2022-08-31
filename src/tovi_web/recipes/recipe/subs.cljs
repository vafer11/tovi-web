(ns tovi-web.recipes.recipe.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
 ::percentage-value
 (fn [db [_ id]]
   (get-in db [:forms :recipe :values :ingredients id :percentage])))

(reg-sub
 ::ingredient-value
 (fn [db [_ id]]
   (get-in db [:forms :recipe :values :ingredients id :ingredient_id])))

(reg-sub
 ::field-value
 (fn [db [_ id]]
   (get-in db [:forms :recipe :values id])))

(reg-sub
 ::name-error-msg
 (fn [db _]
   (get-in db [:forms :recipe :errors :name 0])))

(reg-sub
 ::name-error?
 :<- [::name-error-msg]
 (fn [[error] _]
   (boolean error)))

(reg-sub
 ::steps-error-msg
 (fn [db _]
   (get-in db [:forms :recipe :errors :steps 0])))

(reg-sub
 ::steps-error?
 :<- [::steps-error-msg]
 (fn [[error] _]
   (boolean error)))

(reg-sub
 ::ingredient-percentage-error-msg
 (fn [db [_ id]]
   (get-in db [:forms :recipe :errors :ingredients id :percentage 0])))

(reg-sub
 ::ingredient-percentage-error?
 (fn [[_ id]]
   (subscribe [::ingredient-percentage-error-msg id]))
 (fn [[error-msg] _]
   (boolean error-msg)))

(reg-sub
 ::image-src
 (fn [db _]
   (-> db :forms :recipe :values :image :src)))

(reg-sub
 ::recipe-ingredients
 (fn [db _]
   (-> db :forms :recipe :values :ingredients)))

(reg-sub
 ::ingredients
 (fn [db _]
   (-> db :ingredients)))