(ns tovi-web.views
  (:require 
   ["@mui/material/CssBaseline" :default CssBaseline]
   ["@mui/material/styles" :refer [ThemeProvider]]
   [tovi-web.theme :refer [tovi-theme]]
   [tovi-web.account.views.signup :refer [signup]]
   [tovi-web.account.views.signin :refer [signin]]
   [tovi-web.recipes.views.recipes :refer [recipes]]
   [tovi-web.recipes.views.recipe :refer [recipe]]
   [re-frame.core :as re-frame]
   [tovi-web.account.events :as events]
   [tovi-web.account.subs :as subs]))


(defmulti panels identity)
(defmethod panels :default [] [:div "No panel found for this route."])

;; home

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      (str "Hello from " @name ". This is the Home Page.")]

     [:div
      [:a {:on-click #(re-frame/dispatch [::events/navigate :about])}
       "go to About Page"]]]))

(defmethod panels :home-panel [] [home-panel])

;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:on-click #(re-frame/dispatch [::events/navigate :home])}
     "go to Home Page"]]])

(defmethod panels :about-panel [] [about-panel])

(defmethod panels :signup-panel [] [signup])
(defmethod panels :signin-panel [] [signin])
(defmethod panels :recipes-panel [] [recipes])
(defmethod panels :create-recipe-panel [] [recipe "Create recipe" :create])
(defmethod panels :edit-recipe-panel [] [recipe "Update recipe" :edit])


;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:> ThemeProvider {:theme (tovi-theme)}
     [:<>
      [:> CssBaseline]
      (panels @active-panel)]]))