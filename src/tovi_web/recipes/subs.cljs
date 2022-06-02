(ns tovi-web.recipes.subs
  (:require [re-frame.core :refer [reg-sub]]
            [tovi-web.utils :as utils]))

(reg-sub
 ::recipes
 (fn [db]
   (:recipes db)))

; Used by view-recipe, calculate-recipe, and delete-recipe dialog.
(reg-sub
 ::active-dialog-recipe-id
 (fn [db [_ dialog]]
   (get-in db [:active-dialog dialog :active-recipe-id])))

(reg-sub
 ::recipe-image
 (fn [db _]
   (-> db :forms :recipe :image :src)))

(reg-sub
 ::recipe-form
 (fn [db _]
   (-> db :forms :recipe)))

;; Used by recipe component 
(reg-sub
 ::form-recipe-ingredients
 (fn [db _]
   (-> db :forms :recipe :ingredients utils/format-ingredients)))

(reg-sub
 ::ingredients
 (fn [db _]
   (-> db :ingredients vals vec)))
