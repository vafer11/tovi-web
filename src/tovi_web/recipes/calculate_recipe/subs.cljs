(ns tovi-web.recipes.calculate-recipe.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::recipe-form
 (fn [db _]
   (-> db :forms :recipe)))