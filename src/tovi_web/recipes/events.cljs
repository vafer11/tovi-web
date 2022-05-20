(ns tovi-web.recipes.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [tovi-web.utils :as utils]))

;; Used by edit recipe
(defn- ingredients-to-form [ingredients]
  (reduce-kv 
   (fn [acc k v]
     (->> (utils/to-form-map v [:id :percentage :label :quantity])
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
     (->> (utils/from-form-map v [:id :percentage :label :quantity])
          (assoc acc k)))
   ingredients
   ingredients))

; Used by edit recipe, and create recipe (when the submit btn is clicked)
(defn- recipe-from-form [{:keys [ingredients] :as recipe}]
  (let [ingredients (ingredients-from-form ingredients)
        recipe (utils/from-form-map recipe [:name :description :steps])]
    (assoc recipe :ingredients ingredients)))

;; Used by edit recipe
(reg-event-fx
 ::show-edit-recipe
 (fn [{:keys [db]} [_ id]]
   (let [recipe (-> db (get-in [:recipes id]) recipe-to-form)]
     {:db (assoc-in db [:forms :recipe] recipe)
      :fx [[:dispatch [:navigate :edit-recipe]]]})))

(reg-event-fx
 ::show-recipe
 (fn [{:keys [db]} [_ id mode]]
   (let [recipe (-> db (get-in [:recipes id]) recipe-to-form)]
     {:db (assoc-in db [:forms :recipe] recipe)
      :fx [[:dispatch [:navigate mode]]]})))

;; Used by edit recipe
(reg-event-fx
 ::edit-recipe
 (fn [{:keys [db]} _]
   (let [recipe (-> db :forms :recipe recipe-from-form)
         recipe-key (:id recipe)]
     {:db (assoc-in db [:recipes recipe-key] recipe)
      :fx [[:dispatch [:navigate :recipes]]]})))

;; Used by edit recipe
(reg-event-db
 ::confirm-edit-recipe
 (fn [db [_ id]]
   db))

;; Used by calculate recipe
(reg-event-db
 ::balance-recipe
 (fn [db [_ recipe-id ingredient-id new-value]]
   (let [quantity (get-in db [:recipes recipe-id :ingredients ingredient-id :quantity])
         ingredients-id-to-update (remove #{ingredient-id}
                                          (-> db (get-in [:forms :calculate-recipe :ingredients]) keys))
         factor (/ new-value quantity)
         reduce-fn (fn [acc id]
                     (let [quantity (get-in db [:recipes recipe-id :ingredients id :quantity])]
                       (update-in
                        acc
                        [:forms :calculate-recipe :ingredients id :quantity :value]
                        #(js/parseInt (* quantity factor)))))]

     (reduce reduce-fn db ingredients-id-to-update))))

;; Used by calculate recipe
(reg-event-db
 ::calculate-recipe-dough-weight
 (fn [db _]
   (let [ingredients (get-in db [:forms :calculate-recipe :ingredients])
         dough-weight (reduce-kv
                       (fn [acc _ {:keys [quantity]}] 
                         (+ acc (:value quantity)))
                       0
                       ingredients)]
     (assoc-in db [:forms :calculate-recipe :dough-weight :value] dough-weight))))

(defn recipe-total-percentage [ingredients]
  (reduce-kv 
   (fn [acc _ {:keys [percentage]}]
     (+ acc (:value percentage)))
   0
   ingredients))

;; Used by calculate recipe
(reg-event-db
 ::balance-recipe-by-dough-weigth
 (fn [db [_ total-weigth]]
   (let [ingredients (get-in db [:forms :calculate-recipe :ingredients])
         total-percentage (recipe-total-percentage ingredients)
         reduce-fn (fn [acc k {:keys [percentage]}]
                     (update-in
                      acc
                      [:forms :calculate-recipe :ingredients k :quantity :value]
                      #(-> (utils/rule-of-three total-weigth total-percentage (:value percentage))
                           utils/to-int)))]
     (reduce-kv reduce-fn db ingredients))))

;; Used by calculate recipe
(reg-event-fx
 ::show-calculate-recipe
 (fn [{:keys [db]} [_ id]]
   (let [ingredients (-> db (get-in [:recipes id :ingredients]) ingredients-to-form)]
     {:db (assoc-in db [:forms :calculate-recipe :ingredients] ingredients)
      :fx [[:dispatch [::calculate-recipe-dough-weight]]
           [:dispatch [:show-dialog :calculate-recipe id]]]})))

;; not used yet... should be deleted
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
     (assoc-in db [:forms :recipe :ingredients next-id] {:id {:value next-id}
                                                         :label {:value ""}
                                                         :percentage {:value 0}
                                                         :quantity {:value ""}
                                                         :unit "gr"}))))
(reg-event-fx
 ::create-recipe
 (fn [{:keys [db]} _]
   (let [recipe (-> db :forms :recipe recipe-from-form)
         next-key (->> db :recipes keys (reduce max 0) inc)]
     {:db (assoc-in db [:recipes next-key] (assoc recipe :id next-key))
      :fx [[:dispatch [:navigate :recipes]]]})))

