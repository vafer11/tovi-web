(ns tovi-web.components.events
  (:require [re-frame.core :refer [reg-event-db reg-sub subscribe]]))

(reg-event-db
 :set-input-value
 (fn [db [_ path value]]
   (assoc-in db (conj path :value) value)))

(reg-sub
 :input-value
 (fn [db [_ path]]
   (get-in db (conj path :value))))

(reg-event-db
 :set-input-error
 (fn [db [_ path value]]
   (assoc-in db (conj path :error) value)))

(reg-sub
 :input-error
 (fn [db [_ path]]
   (get-in db (conj path :error))))

(reg-sub
 :input-error?
 (fn [[_ path]]
   (subscribe [:input-error path]))
 (fn [error-msg]
   (not (nil? error-msg))))


(reg-event-db
 :upload-image
 (fn [db [_ path files]]
   (if-let [file (first files)]
     (assoc-in db path {:name (.-name file)
                        :attachment file
                        :src (.createObjectURL js/URL file)})
     db)))

;; Generic event handler to update a path value
(reg-event-db
 :set-path-db-value
 (fn [db [_ path value]]
   (assoc-in db path value)))

;; Generic event handler to get a path value
(reg-sub
 :path-db-value
 (fn [db [_ path]]
   (get-in db path)))