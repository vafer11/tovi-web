(ns tovi-web.recipes.recipe.subs
  (:require [re-frame.core :refer [reg-sub]]
            [tovi-web.utils :as utils]))

(reg-sub
 ::recipe-image
 (fn [db _]
   (-> db :forms :recipe :image :src)))

(reg-sub
 ::form-recipe-ingredients
 (fn [db _]
   (-> db :forms :recipe :ingredients utils/format-ingredients)))

(reg-sub
 ::ingredients
 (fn [db _]
   (-> db :ingredients)))