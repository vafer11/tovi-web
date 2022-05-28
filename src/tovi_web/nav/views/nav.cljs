(ns tovi-web.nav.views.nav
  (:require
   ["@mui/material" :as mui]
   [tovi-web.account.subs :as subs]
   [tovi-web.nav.views.authenticated :refer [authenticated]]
   [re-frame.core :refer [subscribe]]))

(defn public []
  [:> mui/AppBar {:position :static}
   [:> mui/Toolbar
    [:> mui/Typography {:variant :h6 :className "nav-title-class"} "My Recipes"]]])

(defn nav []
  (let [logged-in @(subscribe [::subs/logged-in?])]
    (if logged-in
      [authenticated]
      [public])))