(ns tovi-web.account.signup.views
  (:require
   ["@mui/material" :as mui]
   [tovi-web.account.signup.events :as events]
   [tovi-web.account.signup.subs :as subs]
   [tovi-web.account.signup.db :refer [valid-field?]]
   [re-frame.core :refer [subscribe dispatch]]))

(defn onChange [id input]
  (let [value (.. input -target -value)]
    (dispatch [::events/set-field-value id value])
    (when (valid-field? id {id value})
      (dispatch [::events/dissoc-error id]))))

(defn signup []
  [:> mui/Container {:component :main :maxWidth :xs :style {:margin-top 20}}
   [:> mui/Typography {:component :h1 :variant :h5} "Sign Up"]
   [:form {:noValidate true :autoComplete "off"}
    [:> mui/Grid {:container true :spacing 2}
     [:> mui/Grid {:item true :xs 12} 
      [:> mui/TextField {:id :name
                         :label "Name"
                         :value @(subscribe [::subs/field-value :name])
                         :error @(subscribe [::subs/name-error?])
                         :helperText @(subscribe [::subs/name-error-msg])
                         :onChange #(onChange :name %1)}]]
     [:> mui/Grid {:item true :xs 12}
      [:> mui/TextField {:id :last-name
                         :label "Last Name"
                         :value @(subscribe [::subs/field-value :last-name])
                         :error @(subscribe [::subs/last-name-error?])
                         :helperText @(subscribe [::subs/last-name-error-msg])
                         :onChange #(onChange :last-name %1)}]]
     [:> mui/Grid {:item true :xs 12}
      [:> mui/TextField {:id :email
                         :label "Email"
                         :value @(subscribe [::subs/field-value :email])
                         :error @(subscribe [::subs/email-error?])
                         :helperText @(subscribe [::subs/email-error-msg])
                         :onChange #(onChange :email %1)}]]
     [:> mui/Grid {:item true :xs 12}
      [:> mui/TextField {:id :password
                         :label "Password"
                         :type :password
                         :value @(subscribe [::subs/field-value :password])
                         :error @(subscribe [::subs/password-error?])
                         :helperText @(subscribe [::subs/password-error-msg])
                         :onChange #(onChange :password %1)}]]
     [:> mui/Grid {:item true :xs 12}
      [:> mui/TextField {:id :confirm-password
                         :label "Confirm passwotd"
                         :type :password
                         :value @(subscribe [::subs/field-value :confirm-password])
                         :error @(subscribe [::subs/confirm-password-error?])
                         :helperText @(subscribe [::subs/confirm-password-error-msg])
                         :onChange #(onChange :confirm-password %1)}]]
     [:> mui/Grid {:item true :xs 12 :sx {:mb 4}}
      [:> mui/Button {:variant :contained
                      :fullWidth true
                      :onClick #(dispatch [::events/signup])}
       "Submit"]]]]])