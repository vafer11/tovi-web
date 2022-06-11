(ns tovi-web.account.signin.events
  (:require [re-frame.core :refer [reg-event-db]]
            [tovi-web.account.signin.db :refer [valid-form? validate-form]]))

(reg-event-db
 ::set-field-value
 (fn [db [_ id input]]
   (assoc-in db [:forms :signin :values id] input)))

(reg-event-db
 ::dissoc-error
 (fn [db [_ field]]
   (update-in db [:forms :signin :errors] dissoc field)))

(reg-event-db
 ::signin
 (fn [db _]
   (let [form (get-in db [:forms :signin :values])]
     (if (valid-form? form)
       db
       (let [errors (-> form validate-form first)]
         (assoc-in db [:forms :signin :errors] errors))))))