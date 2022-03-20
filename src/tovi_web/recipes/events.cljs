(ns tovi-web.recipes.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]))


(reg-event-fx
 ::show-edit-recipe
 (fn [{:keys [db]} [_ id]]
   (let [recipe (get-in db [:recipes id])]
     {:db (assoc-in db [:forms :recipe :values] recipe)
      :fx [[:dispatch [:navigate :edit-recipe]]]})))

(reg-event-fx
 ::edit-recipe
 (fn [{:keys [db]} _]
   (let [recipe (-> db :forms :recipe :values)
         recipe-key (:id recipe)]
     {:db (assoc-in db [:recipes recipe-key] recipe)
      :fx [[:dispatch [:navigate :recipes]]]})))


(reg-event-db
 ::confirm-edit-recipe
 (fn [db [_ id]]
   db))

(reg-event-db
 ::calculate-recipe
 (fn [db [_ id]]
   db))

(reg-event-fx
 ::delete-recipe
 (fn [{:keys [db]} [_ id]]
   {:db (update-in db [:recipes] dissoc id)
    :fx [[:dispatch [:hide-dialog]]]}))

(reg-event-db
 ::remove-ingredient-from-recipe
 (fn [db [_ id]]
   (update-in db [:forms :recipe :values :ingredients] dissoc id)))

(reg-event-db
 ::add-ingredient-to-recipe
 (fn [db _]
   (let [ingredients (get-in db [:forms :recipe :values :ingredients])
         next-id (->> ingredients keys (reduce max 0) inc)]
     (assoc-in db [:forms :recipe :values :ingredients next-id] {:id (str next-id)
                                                                 :label ""
                                                                 :percentage 0
                                                                 :quantity ""
                                                                 :unit "gr"}))))

(reg-event-fx
 ::create-recipe
 (fn [{:keys [db]} _]
   (let [recipe (-> db :forms :recipe :values)
         next-key (->> db :recipes keys (reduce max 0) inc)]
     {:db (assoc-in db [:recipes next-key] (assoc recipe :id next-key))
      :fx [[:dispatch [:navigate :recipes]]]})))

