(ns tovi-web.account.signin.events
  (:require [day8.re-frame.http-fx]
            [re-frame.core :refer [reg-event-db reg-event-fx]]
            [ajax.core :refer [json-request-format json-response-format]] 
            [tovi-web.account.signin.db :refer [valid-form? validate-form]]
            [tovi-web.utils :refer [show-msg]]))

(reg-event-db
 ::set-field-value
 (fn [db [_ id input]]
   (assoc-in db [:forms :signin :values id] input)))

(reg-event-db
 ::dissoc-error
 (fn [db [_ field]]
   (update-in db [:forms :signin :errors] dissoc field)))

(defn disable-submit-btn [db]
  (assoc-in db [:forms :signin :submit-btn] {:disabled true :text "Loading..."}))

(defn enable-submit-btn [db]
  (assoc-in db [:forms :signin :submit-btn] {:disabled false :text "Submit"}))

(defn handler-signup-errors [db {:keys [error-key msg]}]
  (case error-key 
    "invalid-email-or-pw" (-> db
                           (assoc-in [:forms :signin :errors :email] msg)
                           (assoc-in [:forms :signin :errors :password] msg))
    (show-msg db "error" "Something went wrong")))

(defn dissoc-errors [db]
  (update-in db [:forms :signup] dissoc :errors))

(reg-event-fx
 ::success-signin
 (fn [{:keys [db]} [_ result]]
   {:db (-> db
            (enable-submit-btn)
            (dissoc-errors)
            (show-msg "success" "Sucsessfully signed up")
            (assoc :account result))
    :fx [[:dispatch [:tovi-web.recipes.recipes.events/load-recipes]]
         [:dispatch [:tovi-web.recipes.recipes.events/load-ingredients]]
         [:dispatch [:navigate :recipes]]]}))

(reg-event-db
 ::failure-signin
 (fn [db [_ result]]
   (.log js/console (str result))
   (-> db
       (enable-submit-btn)
       (handler-signup-errors (:response result)))))

(reg-event-fx
 ::signin
 (fn [{:keys [db]} _]
   (let [form (get-in db [:forms :signin :values])]
     (if (valid-form? form)
       {:db   (disable-submit-btn db)
        :http-xhrio {:method          :post
                     :uri             "http://localhost:3000/api/v1/account/signin"
                     :params          form
                     :format          (json-request-format)
                     :response-format (json-response-format {:keywords? true})
                     :on-success      [::success-signin]
                     :on-failure      [::failure-signin]}}
       (let [errors (validate-form form)]
         {:db (assoc-in db [:forms :signin :errors] errors)})))))