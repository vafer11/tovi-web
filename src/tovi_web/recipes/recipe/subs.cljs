(ns tovi-web.recipes.recipe.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::percentage-value
 (fn [db [_ id]]
   (get-in db [:forms :recipe :values :ingredients id :percentage])))

(reg-sub
 ::field-value
 (fn [db [_ id]]
   (get-in db [:forms :recipe :values id])))

(reg-sub
 ::name-error-msg
 (fn [db _]
   (get-in db [:forms :recipe :errors :name])))

(reg-sub
 ::name-error?
 :<- [::name-error-msg]
 (fn [[error] _]
   (boolean error)))

(reg-sub
 ::steps-error-msg
 (fn [db _]
   (get-in db [:forms :recipe :errors :steps])))

(reg-sub
 ::steps-error?
 :<- [::steps-error-msg]
 (fn [[error] _]
   (boolean error)))


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