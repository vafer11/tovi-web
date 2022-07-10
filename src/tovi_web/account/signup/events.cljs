(ns tovi-web.account.signup.events
  (:require [re-frame.core :refer [reg-event-db]]
            [tovi-web.utils :refer [remove-blank]]
            [tovi-web.account.signup.db :refer [valid-form? validate-form]]))

(reg-event-db
 ::set-field-value
 (fn [db [_ id input]]
   (assoc-in db [:forms :signup :values id] input)))

(reg-event-db
 ::dissoc-error
 (fn [db [_ field]]
   (update-in db [:forms :signup :errors] dissoc field)))

(reg-event-db
 ::signup
 (fn [db _]
   (let [form (get-in db [:forms :signup :values])]
     (if (valid-form? form)
       db
       (let [errors (validate-form form)]
         (assoc-in db [:forms :signup :errors] errors))))))