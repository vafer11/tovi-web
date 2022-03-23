(ns tovi-web.account.views.signin
  (:require ["@mui/material" :as mui]
            [tovi-web.account.events :as events]
            [tovi-web.components.inputs :refer [text-field button]]
            [re-frame.core :refer [dispatch]]))

(defn signin []
  [:> mui/Container {:component :main :maxWidth :xs :style {:margin-top 20}}
   [:> mui/Typography {:component :h1 :variant :h5} "Sign Up"]
   [:form {:noValidate true :autoComplete "off"}
    [text-field
     [:forms :signin :email]
     {:id :email
      :label "Email"
      :required true}]
    [text-field
     [:forms :signin :password]
     {:id :password
      :label "Password"
      :required true}]
    [button
     "Submit"
     {:onClick #(dispatch [::events/submit-signin-form])}]]])