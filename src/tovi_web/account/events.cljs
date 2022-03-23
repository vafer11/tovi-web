(ns tovi-web.account.events
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-event-db
 ::submit-signup-form
 (fn [db _]
   (.log js/console "submit-signup-form")
   (.log js/console (-> db :forms :signup str))
   db))

(re-frame/reg-event-db
 ::submit-signin-form
 (fn [db _]
   (.log js/console "submit-signin-form")
   (.log js/console (-> db :forms :signin str))
   db))