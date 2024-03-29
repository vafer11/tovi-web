(ns tovi-web.account.signin.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::field-value
 (fn [db [_ id]]
   (get-in db [:forms :signin :values id])))

(reg-sub
 ::email-error-msg
 (fn [db _]
   (get-in db [:forms :signin :errors :email])))

(reg-sub
 ::email-error?
 :<- [::email-error-msg]
 (fn [[error] _]
   (boolean error)))

(reg-sub
 ::password-error-msg
 (fn [db _]
   (get-in db [:forms :signin :errors :password])))

(reg-sub
 ::password-error?
 :<- [::password-error-msg]
 (fn [[error] _] (boolean error)))

(reg-sub
 ::uid
 (fn [db _] (get-in db [:account :id])))

(reg-sub
 ::user-information
 (fn [db _] (get-in db [:account])))

(reg-sub
 ::logged-in?
 :<- [::uid]
 (fn [id] (boolean id)))

(reg-sub
 ::submit-btn-status
 (fn [db _]
   (get-in db [:forms :signin :submit-btn :disabled])))

(reg-sub
 ::submit-btn-text
 (fn [db _]
   (get-in db [:forms :signin :submit-btn :text])))