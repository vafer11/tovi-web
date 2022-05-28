(ns tovi-web.account.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::uid
 (fn [db _] (get-in db [:account :uid])))

(reg-sub
 ::logged-in?
 :<- [::uid]
 (fn [uid] (boolean uid)))