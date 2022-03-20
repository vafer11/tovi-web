(ns tovi-web.account.events
  (:require
   [re-frame.core :as re-frame]))

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