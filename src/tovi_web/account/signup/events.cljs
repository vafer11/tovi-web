(ns tovi-web.account.signup.events
  (:require [day8.re-frame.http-fx]
            [re-frame.core :refer [reg-event-db reg-event-fx]]
            [ajax.core :refer [json-request-format json-response-format]]
            [tovi-web.utils :refer [show-msg]]
            [tovi-web.account.signup.db :refer [valid-form? validate-form]]))

(reg-event-db
 ::set-field-value
 (fn [db [_ id input]]
   (assoc-in db [:forms :signup :values id] input)))

(reg-event-db
 ::dissoc-error
 (fn [db [_ field]]
   (update-in db [:forms :signup :errors] dissoc field)))

(defn disable-submit-btn [db]
  (assoc-in db [:forms :signup :submit-btn] {:disabled true :text "Loading..."}))

(defn enable-submit-btn [db]
  (assoc-in db [:forms :signup :submit-btn] {:disabled false :text "Submit"}))

(defn handler-signup-errors [db {:keys [error-key msg]}]
  (case error-key
    "users-email-key" (assoc-in db [:forms :signup :errors :email] msg)
    "users-phone-key" (show-msg db "error" msg) 
    (show-msg db "error" "Something went wrong")))

(defn dissoc-errors [db]
  (update-in db [:forms :signup] dissoc :errors))

(reg-event-fx
 ::success-signup
 (fn [{:keys [db]} [_ result]]
   {:db (-> db
         (enable-submit-btn)
         (dissoc-errors)
         (show-msg "success" "Sucsessfully signed up")
         (assoc :account result))
    :fx [[:dispatch [:navigate :recipes]]]}))

(reg-event-db
 ::failure-signup
 (fn [db [_ result]]
   (-> db 
    (enable-submit-btn) 
    (handler-signup-errors (:response result)))))

(reg-event-fx
 ::signup
 (fn [{:keys [db]} _]
   (let [form (get-in db [:forms :signup :values])]
     (if (valid-form? form) 
       {:db   (disable-submit-btn db)
        :http-xhrio {:method          :post
                     :uri             "http://localhost:3000/api/v1/account/signup"
                     :params          form
                     :format          (json-request-format)
                     :response-format (json-response-format {:keywords? true})
                     :on-success      [::success-signup]
                     :on-failure      [::failure-signup]}}
       (let [errors (validate-form form)]
         {:db (assoc-in db [:forms :signup :errors] errors)})))))