(ns tovi-web.recipes.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [tovi-web.utils :as utils]))

;; Used by edit recipe
(defn- ingredients-to-form [ingredients]
  (reduce-kv 
   (fn [acc k v]
     (->> (utils/to-form-map v [:id :percentage :label])
          (assoc acc k)))
   ingredients
   ingredients))

;; Used by edit recipe
(defn- recipe-to-form [{:keys [ingredients] :as recipe}]
  (let [ingredients (ingredients-to-form ingredients)
        recipe (utils/to-form-map recipe [:name :description :steps])]
    (assoc recipe :ingredients ingredients)))

; Used by edit recipe, and create recipe (when the submit btn is clicked)
(defn- ingredients-from-form [ingredients]
  (reduce-kv
   (fn [acc k v]
     (->> (utils/from-form-map v [:id :percentage :label])
          (assoc acc k)))
   ingredients
   ingredients))

; Used by edit recipe, and create recipe (when the submit btn is clicked)
(defn- recipe-from-form [{:keys [ingredients] :as recipe}]
  (let [ingredients (ingredients-from-form ingredients)
        recipe (utils/from-form-map recipe [:name :description :steps])]
    (assoc recipe :ingredients ingredients)))

(reg-event-fx
 ::show-edit-recipe
 (fn [{:keys [db]} [_ id]]
   (let [recipe (-> db (get-in [:recipes id]) recipe-to-form)]
     {:db (assoc-in db [:forms :recipe] recipe)
      :fx [[:dispatch [:navigate :edit-recipe]]]})))

(reg-event-fx
 ::edit-recipe
 (fn [{:keys [db]} _]
   (let [recipe (-> db :forms :recipe recipe-from-form)
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
   (update-in db [:forms :recipe :ingredients] dissoc id)))

(reg-event-db
 ::add-ingredient-to-recipe
 (fn [db _]
   (let [ingredients (get-in db [:forms :recipe :ingredients])
         next-id (->> ingredients keys (reduce max 0) inc)]
     (assoc-in db [:forms :recipe :values :ingredients next-id] {:id (str next-id)
                                                                 :label ""
                                                                 :percentage 0
                                                                 :quantity ""
                                                                 :unit "gr"}))))
(reg-event-fx
 ::create-recipe
 (fn [{:keys [db]} _]
   (let [recipe (-> db :forms :recipe recipe-from-form)
         next-key (->> db :recipes keys (reduce max 0) inc)]
     {:db (assoc-in db [:recipes next-key] (assoc recipe :id next-key))
      :fx [[:dispatch [:navigate :recipes]]]})))

