(ns tovi-web.account.events
  (:require
   [re-frame.core :as re-frame]
   [tovi-web.db :as db]))

(re-frame/reg-event-db
 ::submit-signup-form
 (fn [db _]
   (.log js/console "submit-signup-form")
   db))

(re-frame/reg-event-db
 ::submit-signin-form
 (fn [db _]
   (.log js/console "submit-signin-form")
   db))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx
 ::navigate
 (fn [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))