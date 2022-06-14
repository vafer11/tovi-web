(ns tovi-web.recipes.recipes.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]))

(reg-event-db
 ::hide-delete-dialog
 (fn [db _]
   (update-in db [:active-dialog] dissoc :delete-recipe)))

(reg-event-db
 ::show-delete-dialog
 (fn [db [_ id]]
   (assoc-in db [:active-dialog :delete-recipe :id] id)))

(reg-event-fx
 ::show-recipe
 (fn [{:keys [db]} [_ id mode]]
   (let [recipe (get-in db [:recipes id])]
     {:db (assoc-in db [:forms :recipe :values] recipe)
      :fx [(when (= mode :calculate-recipe)
             [:dispatch [:tovi-web.recipes.calculate-recipe.events/calculate-recipe-dough-weight]])
           [:dispatch [:navigate mode]]]})))

(reg-event-fx
 ::delete-recipe
 (fn [{:keys [db]} [_ id]]
   {:db (update-in db [:recipes] dissoc id)
    :fx [[:dispatch [::hide-delete-dialog]]]}))