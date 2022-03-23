(ns tovi-web.account.views.signup
  (:require
   ["@mui/material/Container" :default Container]
   ["@mui/material/Typography" :default Typography]
   [tovi-web.account.events :as events]
   [tovi-web.components.inputs :refer [text-field button]]
   [re-frame.core :refer [dispatch]]))

(defn signup []
  [:> Container {:component :main :maxWidth :xs :style {:margin-top 20}}
   [:> Typography {:component :h1 :variant :h5} "Sign Up"]
   [:form {:noValidate true :autoComplete "off"}
    [text-field
     [:forms :signup :first-name] 
     {:id :first-name
      :label "First name"
      :required true
      :autoFocus true}]
    [text-field
     [:forms :signup :last-name]
     {:id :last-name
      :label "Last name"
      :required true}]
    [text-field 
     [:forms :signup :email]
     {:id :email
      :label "Email"
      :required true}]
    [text-field 
     [:forms :signup :password]
     {:id :password
      :label "Password"
      :required true}]
    [text-field
     [:forms :signup :confirm-password]
     {:id :confirm-password
      :label "Confirm password"
      :required true}]
    [button 
     "Submit" 
     {:onClick #(dispatch [::events/submit-signup-form])}]]])
