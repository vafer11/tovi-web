(ns tovi-web.account.signin.views
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/Visibility" :default VisibilityIcon]
            [tovi-web.account.signin.db :refer [valid-input?]]
            [tovi-web.account.signin.events :as events]
            [tovi-web.account.signin.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]))

(defn onChange [id input]
  (let [value (.. input -target -value)]
    (dispatch [::events/set-field-value id value])
    (when (valid-input? id value)
      (dispatch [::events/dissoc-error id]))))

(defn change-pw-type [_]
  (let [e (.getElementById js/document "password")
        type (.-type e)]
    (if (= "password" type)
      (set! (.-type e) "text")
      (set! (.-type e) "password"))))

(defn signin []
  [:> mui/Container {:component :main :maxWidth :xs :style {:margin-top 20}}
   [:> mui/Typography {:component :h1 :variant :h5} "Sign In"]
   [:form {:noValidate true :autoComplete "off"}
    [:> mui/Grid {:container true :spacing 2}
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
                         :InputProps {:endAdornment 
                                      (as-element [:> mui/InputAdornment {:position "end"}
                                                   [:> mui/IconButton {:onClick change-pw-type}
                                                    [:> VisibilityIcon]]])}
                         :onChange #(onChange :password %1)}]]
     [:> mui/Grid {:item true :xs 12}
      [:> mui/Button {:variant :contained
                      :fullWidth true
                      :disabled @(subscribe [::subs/submit-btn-status])
                      :onClick #(dispatch [::events/signin])}
       @(subscribe [::subs/submit-btn-text])]]]]])