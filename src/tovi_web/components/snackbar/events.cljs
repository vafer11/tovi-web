(ns tovi-web.components.snackbar.events
  (:require [re-frame.core :refer [reg-event-db reg-sub]]))

(reg-event-db
 ::close-snackbar
 (fn [db _] (assoc-in db [:snackbar :open?] false)))

(reg-sub
 ::snackbar
 (fn [db _] (:snackbar db)))