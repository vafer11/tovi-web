(ns tovi-web.recipes.recipes.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::recipes
 (fn [db]
   (:recipes db)))

; Used by view-recipe, calculate-recipe, and delete-recipe dialog.
(reg-sub
 ::active-dialog-recipe-id
 (fn [db [_ dialog]]
   (get-in db [:active-dialog dialog :active-recipe-id])))