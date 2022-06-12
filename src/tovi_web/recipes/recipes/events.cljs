(ns tovi-web.recipes.recipes.events
  (:require [re-frame.core :refer [reg-event-fx]]
            [tovi-web.utils :as utils]))

(defn- ingredients-to-form [ingredients]
  (reduce-kv
   (fn [acc k v]
     (->> (utils/to-form-map v [:id :percentage :label :quantity])
          (assoc acc k)))
   ingredients
   ingredients))

(defn- recipe-to-form [{:keys [ingredients] :as recipe}]
  (let [ingredients (ingredients-to-form ingredients)
        recipe (utils/to-form-map recipe [:name :description :steps])]
    (assoc recipe :ingredients ingredients)))

(reg-event-fx
 ::show-recipe
 (fn [{:keys [db]} [_ id mode]]
   (let [recipe (-> db (get-in [:recipes id]) recipe-to-form)]
     {:db (assoc-in db [:forms :recipe] recipe)
      :fx [(when (= mode :calculate-recipe)
             [:dispatch [:tovi-web.recipes.calculate-recipe.events/calculate-recipe-dough-weight]])
           [:dispatch [:navigate mode]]]})))

(reg-event-fx
 ::delete-recipe
 (fn [{:keys [db]} [_ id]]
   {:db (update-in db [:recipes] dissoc id)
    :fx [[:dispatch [:hide-dialog]]]}))