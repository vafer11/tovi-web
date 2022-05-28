(ns tovi-web.views
  (:require 
   ["@mui/material/CssBaseline" :default CssBaseline]
   ["@mui/material/styles" :refer [ThemeProvider]]
   [tovi-web.theme :refer [tovi-theme]]
   [tovi-web.account.views.signup :refer [signup]]
   [tovi-web.account.views.signin :refer [signin]]
   [tovi-web.recipes.views.recipes :refer [recipes]]
   [tovi-web.recipes.views.recipe :refer [recipe]]
   [tovi-web.recipes.views.calculate-recipe :refer [calculate-recipe]]
   [tovi-web.nav.views.nav :refer [nav]]
   [tovi-web.nav.events :as events]
   [tovi-web.nav.subs :as subs]
   [re-frame.core :as re-frame]))


(defmulti panels identity)
(defmethod panels :default [] [:div "No panel found for this route."])

;; home

(defn home []
  (let [name "name"]
    [:div
     [:h1
      (str "Hello from... " name ". This is the Home Page.")
      ]

     [:div
      [:a {:on-click #(re-frame/dispatch [:navigate :about])}
       "go to About Page"]]]))

(defmethod panels :home [] [home])

;; about

(defn about []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:on-click #(re-frame/dispatch [:navigate :home])}
     "go to Home Page"]]])

(defmethod panels :about [] [about])

(defmethod panels :signup [] [signup])
(defmethod panels :signin [] [signin])
(defmethod panels :recipes [] [recipes])
(defmethod panels :create-recipe [] [recipe "Create recipe" :create])
(defmethod panels :view-recipe [] [recipe "View recipe" :view])
(defmethod panels :edit-recipe [] [recipe "Update recipe" :edit])
(defmethod panels :calculate-recipe [] [calculate-recipe])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    [:> ThemeProvider {:theme (tovi-theme)}
     [:<>
      [:> CssBaseline]
      [nav]      
      (panels @active-panel)]]))