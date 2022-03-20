(ns tovi-web.nav.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

;; Subscription used by view, calculate, and delete recipe dialog components
(reg-sub
 :show-dialog?
 (fn [db [_ dialog]]
   (-> db :active-dialog dialog nil? not)))