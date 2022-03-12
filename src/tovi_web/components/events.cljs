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


(reg-event-db
 ::upload-image
 (fn [db [_ path files]]
   (if-let [file (first files)]
     (assoc-in db path {:name (.-name file)
                        :attachment file
                        :src (.createObjectURL js/URL file)})
     db)))

(reg-sub
 ::image
 (fn [db _]
   (-> db :forms :recipe :values :image)))