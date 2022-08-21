(ns tovi-web.core
  (:require
   ["@mui/material/CssBaseline" :default CssBaseline]
   ["@mui/material/styles" :refer [ThemeProvider]] 
   [re-frame.core :as rf]
   [reagent.dom :as rdom] 
   [tovi-web.db :as db-events]
   [tovi-web.routes :as routes]
   [tovi-web.config :as config]
   [tovi-web.theme :refer [tovi-theme]]
   [tovi-web.account.signup.views :refer [signup]]
   [tovi-web.account.signin.views :refer [signin]]
   [tovi-web.recipes.recipes.views :refer [recipes]]
   [tovi-web.recipes.recipe.views :refer [recipe]]
   [tovi-web.recipes.calculate-recipe.views :refer [calculate-recipe]]
   [tovi-web.components.nav.views :refer [nav]]
   [tovi-web.components.snackbar.views :refer [snackbar]]
   [tovi-web.components.nav.events :as events]
   ))

(defmulti panels identity)
(defmethod panels :default [] [:div "No panel found for this route."])
(defmethod panels :signup [] [signup])
(defmethod panels :signin [] [signin])
(defmethod panels :recipes [] [recipes])
(defmethod panels :create-recipe [] [recipe "Create recipe" :create])
(defmethod panels :view-recipe [] [recipe "View recipe" :view])
(defmethod panels :edit-recipe [] [recipe "Update recipe" :edit])
(defmethod panels :calculate-recipe [] [calculate-recipe])

;; main
(defn main-panel []
  (let [active-panel (rf/subscribe [:active-panel])]
    [:> ThemeProvider {:theme (tovi-theme)}
     [:<>
      [:> CssBaseline] 
      [nav]
      [snackbar]
      (panels @active-panel)]]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))

(defn init []
  (routes/start!)
  (rf/dispatch-sync [::db-events/initialize-db])
  (dev-setup)
  (mount-root))
