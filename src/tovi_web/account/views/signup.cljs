(ns tovi-web.account.views.signup
  (:require
   ["@mui/material/Container" :default Container]
   ["@mui/material/Typography" :default Typography]
   [tovi-web.components.inputs :refer [text-field button]]
   [re-frame.core :refer [dispatch]]))

(defn signup []
  (let [path [:forms :signup]]
    [:> Container {:component :main :maxWidth :xs :style {:margin-top 20}}
     [:> Typography {:component :h1 :variant :h5} "Sign Up"]
     [:form {:noValidate true :autoComplete "off"}
      [text-field path {:id :first-name
                        :label "First name"
                        :required true
                        :autoFocus true}]
      [text-field path {:id :last-name
                        :label "Last name"
                        :required true}]
      [text-field path {:id :email
                        :label "Email"
                        :required true}]
      [text-field path {:id :password
                        :label "Password"
                        :required true}]
      [text-field path {:id :confirm-password
                        :label "Confirm password"
                        :required true}]
      [button "Submit" {:onClick #(dispatch [:tovi-web.account.events/submit-signup-form])}]]]))
