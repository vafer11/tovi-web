(ns tovi-web.recipes.recipes.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::recipes
 (fn [db]
   (:recipes db)))

(reg-sub
 ::show-delete-dialog?
 (fn [db [_]]
   (-> db :active-dialog :delete-recipe boolean)))

(reg-sub
 ::delete-dialog-recipe-id
 (fn [db [_]]
   (-> db :active-dialog :delete-recipe :id)))