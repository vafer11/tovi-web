(ns tovi-web.account.signup.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::field-value
 (fn [db [_ id]]
   (get-in db [:forms :signup :values id])))

(reg-sub
 ::name-error-msg
 (fn [db _]
   (get-in db [:forms :signup :errors :name])))

(reg-sub
 ::name-error?
 :<- [::name-error-msg]
 (fn [[error] _]
   (boolean error)))

(reg-sub
 ::last-name-error-msg
 (fn [db _]
   (get-in db [:forms :signup :errors :last-name])))

(reg-sub
 ::last-name-error?
 :<- [::last-name-error-msg]
 (fn [[error] _]
   (boolean error)))

(reg-sub
 ::email-error-msg
 (fn [db _]
   (get-in db [:forms :signup :errors :email])))

(reg-sub
 ::email-error?
 :<- [::email-error-msg]
 (fn [[error] _]
   (boolean error)))

(reg-sub
 ::password-error-msg
 (fn [db _]
   (get-in db [:forms :signup :errors :password])))

(reg-sub
 ::password-error?
 :<- [::password-error-msg]
 (fn [[error] _] (boolean error)))

(reg-sub
 ::confirm-password-error-msg
 (fn [db _]
   (get-in db [:forms :signup :errors :confirm-password])))

(reg-sub
 ::confirm-password-error?
 :<- [::confirm-password-error-msg]
 (fn [[error] _] (boolean error)))
