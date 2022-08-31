(ns tovi-web.recipes.recipe.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [ajax.core :refer [json-request-format json-response-format]]
            [tovi-web.recipes.recipe.db :refer [valid-form? validate-form]]
            [tovi-web.utils :refer [show-msg]]))

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
   (assoc-in db [:forms :recipe :values :ingredients id :ingredient_id] input)))

(reg-event-db
 ::set-ingredient-label
 (fn [db [_ id input]]
   (assoc-in db [:forms :recipe :values :ingredients id :name] input)))

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
 (fn [db [_ ri_id field]]
   (update-in db [:forms :recipe :errors :ingredients ri_id] dissoc field)))

(reg-event-db
 ::remove-ingredient-from-recipe
 (fn [db [_ id]]
   (update-in db [:forms :recipe :values :ingredients] dissoc id)))

(reg-event-db
 ::add-ingredient-to-recipe
 (fn [db _]
   (let [recipe-ingredients (get-in db [:forms :recipe :values :ingredients])
         {id :id name :name} (-> db :ingredients vals first)
         next-id (->> recipe-ingredients keys (reduce max 0) inc)]
     (assoc-in db [:forms :recipe :values :ingredients next-id] {:ingredient_id id
                                                                 :name name
                                                                 :percentage 0
                                                                 :quantity 0
                                                                 :operation "insert"}))))

(reg-event-db
 ::success-create-recipe
 (fn [db [_ next-key result]]
   (.log js/console (str "next-key: " next-key))
   (.log js/console (str result))))

(reg-event-db
 ::failure-create-recipe
 (fn [db [_ next-key result]]
   (.log js/console (str "next-key: " next-key))
   (.log js/console (str result))
   (show-msg db "error" "Recipe could not be created.")))

(defn- format-create-recipe [db {:keys [ingredients] :as recipe}]
  (let [user_id (-> db :account :id)
        ingredients (reduce 
                     #(conj %1 (-> %2 
                                   (dissoc :name)
                                   (dissoc :operation))) 
                     [] 
                     (vals ingredients))]
    (-> recipe
        (assoc :user_id user_id)
        (assoc :ingredients ingredients))))

(reg-event-fx
 ::create-recipe
 (fn [{:keys [db]} _]
   (let [form (-> db :forms :recipe :values)
         next-key (->> db :recipes keys (reduce max 0) inc)]
     (if (valid-form? form)
       {:db (assoc-in db [:recipes next-key] (assoc form :id next-key))
        :http-xhrio {:method          :post
                     :uri             "http://localhost:3000/api/v1/recipes"
                     :headers         {:authorization (-> db :account :token)}
                     :params          (format-create-recipe db form)
                     :format          (json-request-format)
                     :response-format (json-response-format {:keywords? true})
                     :on-success      [::success-create-recipe next-key]
                     :on-failure      [::failure-create-recipe next-key]}
        :fx [[:dispatch [:navigate :recipes]]]}
       (let [errors (validate-form form)]
         {:db (assoc-in db [:forms :recipe :errors] errors)})))))


(defn- format-edit-recipe [{:keys [ingredients] :as form}]
  (let [ingredients (->> (vals ingredients)
                         (filter (fn [{:keys [operation]}] (not (nil? operation))))
                         (reduce #(conj %1 (-> %2 (dissoc :name))) [])) 
        recipe (-> form
                   (dissoc :id)
                   (dissoc :user_id)
                   (assoc :ingredients ingredients))]
    (println recipe)
    recipe))

(reg-event-db
 ::failure-edit-recipe
 (fn [db [_ result]]
   (.log js/console (str result))
   (show-msg db "error" "Recipe could not be edited.")))

(reg-event-db
 ::success-edit-recipe
 (fn [db [_ _]]
   (show-msg db "success" "Recipe successfully edited.")))

(reg-event-fx
 ::edit-recipe
 (fn [{:keys [db]} _] 
   (let [form (-> db :forms :recipe :values)
         id (:id form)]
     (if (valid-form? form)
       {:db (assoc-in db [:recipes id] form)
        :http-xhrio {:method          :put
                     :uri             (str "http://localhost:3000/api/v1/recipes/" id)
                     :headers         {:authorization (-> db :account :token)}
                     :params          (format-edit-recipe form)
                     :format          (json-request-format)
                     :response-format (json-response-format {:keywords? true})
                     :on-success      [::success-edit-recipe]
                     :on-failure      [::failure-edit-recipe]}
        :fx [[:dispatch [:navigate :recipes]]]}
       (let [errors (validate-form form)]
         {:db (assoc-in db [:forms :recipe :errors] errors)})))))