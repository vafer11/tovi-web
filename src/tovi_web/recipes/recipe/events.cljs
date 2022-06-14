(ns tovi-web.recipes.recipe.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]))

(reg-event-db
 ::set-percentage-value
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values :ingredients id :percentage] input)))

(reg-event-db
 ::set-quantity-value
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values :ingredients id :quantity] input)))

(reg-event-db
 ::set-field-value
 (fn [db [_ field input]]
   (assoc-in db [:forms :recipe :values field] input)))

(reg-event-db
 ::dissoc-error
 (fn [db [_ field]]
   (update-in db [:forms :recipe :errors] dissoc field)))

(reg-event-db
 ::remove-ingredient-from-recipe
 (fn [db [_ id]]
   (update-in db [:forms :recipe :values :ingredients] dissoc id)))

(reg-event-db
 ::add-ingredient-to-recipe
 (fn [db _]
   (let [ingredients (get-in db [:forms :recipe :values :ingredients])
         next-id (->> ingredients keys (reduce max 0) inc)]
     (assoc-in db [:forms :recipe :values :ingredients next-id] {:id next-id
                                                                 :label ""
                                                                 :percentage 0
                                                                 :quantity ""}))))
(reg-event-fx
 ::create-recipe
 (fn [{:keys [db]} _]
   (let [recipe (-> db :forms :recipe :values)
         next-key (->> db :recipes keys (reduce max 0) inc)]
     {:db (assoc-in db [:recipes next-key] (assoc recipe :id next-key))
      :fx [[:dispatch [:navigate :recipes]]]})))

(reg-event-fx
 ::edit-recipe
 (fn [{:keys [db]} _]
   (let [recipe (-> db :forms :recipe :values)
         recipe-key (:id recipe)]
     {:db (assoc-in db [:recipes recipe-key] recipe)
      :fx [[:dispatch [:navigate :recipes]]]})))