(ns tovi-web.recipes.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::recipes
 (fn [db _]
   (:recipes db)))