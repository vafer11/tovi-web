(ns tovi-web.nav.events
  (:require
   [tovi-web.routes :as routes]
   [re-frame.core :refer [reg-event-fx reg-fx reg-event-db]]))

;; :navigate Effect handler
(reg-fx
 :navigate-to
 (fn [handler]
   (routes/navigate! handler)))

;; :navigate Event handler
(reg-event-fx
 :navigate
 (fn [_ [_ handler]]
   {:navigate-to handler}))

(reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(reg-event-db
 :show-dialog
 (fn [db [_ dialog id]]
   (assoc-in db [:active-dialog dialog] {:active-recipe-id id})))

(reg-event-db
 :hide-dialog
 (fn [db]
   (dissoc db :active-dialog)))