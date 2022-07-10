(ns tovi-web.recipes.recipe.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [tovi-web.recipes.recipe.db :refer [valid-form? validate-form]]))

(reg-event-db
 ::set-percentage
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values :ingredients id :percentage] input)))

(reg-event-db
 ::set-quantity
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values :ingredients id :quantity] input)))

(reg-event-db
 ::set-ingredient-id
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values :ingredients id :id] input)))

(reg-event-db
 ::set-ingredient-label
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values :ingredients id :label] input)))

(reg-event-db
 ::set-field-value
 (fn [db [_ field input]]
   (assoc-in db [:forms :recipe :values field] input)))

(reg-event-db
 ::upload-image
 (fn [db [_ files]]
   (if-let [file (first files)]
     (assoc-in db [:forms :recipe :values :image] {:name (.-name file)
                                                   :attachment file
                                                   :src (.createObjectURL js/URL file)})
     db)))

(reg-event-db
 ::dissoc-error
 (fn [db [_ field]]
   (update-in db [:forms :recipe :errors] dissoc field)))

(reg-event-db
 ::dissoc-ingredient-error
 (fn [db [_ ingredient-id field]]
   (.log js/console (str ingredient-id))
   (update-in db [:forms :recipe :errors :ingredients ingredient-id] dissoc field)))

(reg-event-db
 ::remove-ingredient-from-recipe
 (fn [db [_ id]]
   (update-in db [:forms :recipe :values :ingredients] dissoc id)))

(reg-event-db
 ::add-ingredient-to-recipe
 (fn [db _]
   (let [ingredients (get-in db [:forms :recipe :values :ingredients])
         next-id (->> ingredients keys (reduce max 0) inc)]
     (assoc-in db [:forms :recipe :values :ingredients next-id] {:id 1
                                                                 :label "Harina"
                                                                 :percentage 0
                                                                 :quantity ""}))))
(reg-event-fx
 ::create-recipe
 (fn [{:keys [db]} _]
   (let [form (-> db :forms :recipe :values)
         next-key (->> db :recipes keys (reduce max 0) inc)]
     (if (valid-form? form) 
       {:db (assoc-in db [:recipes next-key] (assoc form :id next-key))
        :fx [[:dispatch [:navigate :recipes]]]}
       (let [errors (validate-form form)]
         {:db (assoc-in db [:forms :recipe :errors] errors)})))))

(reg-event-fx
 ::edit-recipe
 (fn [{:keys [db]} _]
   (let [form (-> db :forms :recipe :values)
         recipe-key (:id form)]
     (if (valid-form? form)
       {:db (assoc-in db [:recipes recipe-key] form)
        :fx [[:dispatch [:navigate :recipes]]]}
       (let [errors (validate-form form)]
         {:db (assoc-in db [:forms :recipe :errors] errors)})))))