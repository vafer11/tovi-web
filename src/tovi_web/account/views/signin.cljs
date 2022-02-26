(ns tovi-web.account.views.signin
  (:require ["@mui/material" :as mui]
            [tovi-web.components.inputs :refer [text-field button]]
            [re-frame.core :refer [dispatch]]))

(defn signin []
  (let [path [:forms :signin]]
    [:> mui/Container {:component :main :maxWidth :xs :style {:margin-top 20}}
     [:> mui/Typography {:component :h1 :variant :h5} "Sign Up"]
     [:form {:noValidate true :autoComplete "off"}
      [text-field path {:id :email
                        :label "Email"
                        :required true}]
      [text-field path {:id :password
                        :label "Password"
                        :required true}]
      [button "Submit" {:onClick #(dispatch [:tovi-web.account.events/submit-signin-form])}]]]))