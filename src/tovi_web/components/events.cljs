(ns tovi-web.components.events
  (:require [re-frame.core :refer [reg-event-db reg-sub subscribe]]))

(reg-event-db
 ::set-path-db-value
 (fn [db [_ path value]]
   (assoc-in db path value)))

(reg-sub
 ::path-db-value
 (fn [db [_ path]]
   (get-in db path)))

(reg-sub
 ::field-error?
 (fn [[_ path]]
   (subscribe [::path-db-value path]))
 (fn [error-msg]
   (not (nil? error-msg))))