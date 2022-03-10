(ns tovi-web.recipes.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]))

(reg-event-db
 ::show-recipe-dialog
 (fn [db [_ dialog id]]
   (assoc db :active-dialog {:name dialog
                             :recipe-id id})))
(reg-event-db
 ::hide-recipe-dialog
 (fn [db]
   (dissoc db :active-dialog)))

(reg-event-fx
 ::edit-recipe
 (fn [{:keys [db]} [_ id]]
   (let [recipe (get-in db [:recipes id])]
     {:db (assoc-in db [:forms :edit-recipe :values] recipe)
      :fx [[:dispatch [::show-recipe-dialog :edit id]]]})))


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
    :fx [[:dispatch [::hide-recipe-dialog :delete]]]}))

(reg-event-db
 ::remove-ingredient-from-recipe
 (fn [db [_ id]]
   (update-in db [:forms :recipe :values :ingredients] dissoc id)))