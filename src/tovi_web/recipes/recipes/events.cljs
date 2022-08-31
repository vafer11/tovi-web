(ns tovi-web.recipes.recipes.events
  (:require [ajax.core :refer [json-request-format json-response-format]]
            [re-frame.core :refer [reg-event-db reg-event-fx]]
            [tovi-web.utils :refer [show-msg convert-to-map]]))

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

(reg-event-db
 ::success-delete-recipe
 (fn [db [_ _]] 
   (show-msg db "success" "Recipes successfully deleted")))

(reg-event-db
 ::failure-delete-recipe
 (fn [db [_ result]]
   (.log js/console (str result))
   (show-msg db "error" "Recipes could not be deleted")))


(reg-event-fx
 ::delete-recipe
 (fn [{:keys [db]} [_ id]]
   {:db (update-in db [:recipes] dissoc id)
    :http-xhrio {:method          :delete
                 :uri             (str "http://localhost:3000/api/v1/recipes/" id)
                 :headers         {:authorization (-> db :account :token)}
                 :format          (json-request-format)
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [::success-delete-recipe]
                 :on-failure      [::failure-delete-recipe]}
    :fx [[:dispatch [::hide-delete-dialog]]]}))

(reg-event-db
 ::success-load-recipes
 (fn [db [_ result]] 
   (let [recipes (convert-to-map result :id)
         formatted-recipes-ingredients (reduce-kv
                                        (fn [acc k _]
                                          (update-in acc [k :ingredients] #(convert-to-map %1 :ri_id)))
                                        recipes
                                        recipes)]
     (assoc db :recipes formatted-recipes-ingredients))))

(reg-event-db
 ::failure-load-recipes
 (fn [db [_ result]]
   (.log js/console (str result))
   (show-msg db "error" "Recipes could not been loaded.")))

(reg-event-fx
 ::load-recipes
 (fn [{:keys [db]} _]
   {:http-xhrio {:method          :get
                 :uri             "http://localhost:3000/api/v1/recipes"
                 :headers         {:authorization (-> db :account :token)}
                 :format          (json-request-format)
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [::success-load-recipes]
                 :on-failure      [::failure-load-recipes]}}))

(reg-event-db
 ::success-load-ingredients
 (fn [db [_ result]]
   (let [formatted-ingredients (convert-to-map result :id)]
     (assoc db :ingredients formatted-ingredients))))

(reg-event-db
 ::failure-load-ingredients
 (fn [db [_ result]]
   (.log js/console (str result))
   (show-msg db "error" "Ingredients could not been loaded.")))

(reg-event-fx
 ::load-ingredients
 (fn [{:keys [db]} _]
   {:http-xhrio {:method          :get
                 :uri             "http://localhost:3000/api/v1/ingredients"
                 :headers         {:authorization (-> db :account :token)}
                 :format          (json-request-format)
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [::success-load-ingredients]
                 :on-failure      [::failure-load-ingredients]}}))