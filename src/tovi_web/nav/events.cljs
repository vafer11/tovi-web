(ns tovi-web.nav.events
  (:require
   [tovi-web.routes :as routes]
   [re-frame.core :refer [reg-event-fx reg-fx reg-event-db]]))

;; :navigate effect handler
(reg-fx
 :navigate-to
 (fn [handler]
   (routes/navigate! handler)))

;; :navigate event handler
(reg-event-fx
 :navigate
 (fn [_ [_ handler]]
   {:navigate-to handler}))

(reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))