(ns tovi-web.account.signup.db
  (:require [malli.core :as m]
            [tovi-web.utils :as utils]))

(def name-schema (utils/text-field 2 90))
(def email-schema [:re {:error/message "Invalid email"} utils/email-regex])
(def password-schema (utils/text-field 6 16))

(def signup-schema
  [:and
   [:map
    [:name name-schema]
    [:last_name name-schema]
    [:email email-schema]
    [:password password-schema]
    [:confirm_pw password-schema]]
   [:fn {:error/message "Password don't match" :error/path [:confirm_pw]}
    (fn [{:keys [password confirm_pw]}] (= password confirm_pw))]])

(defn valid-form? [form]
  (m/validate signup-schema form))

(defn validate-form [form]
  (utils/validate-schema signup-schema form))

(defn valid-input? [field-name input]
  (case field-name
    :name (m/validate name-schema input)
    :last_name (m/validate name-schema input)
    :email (m/validate email-schema input)
    :password (m/validate password-schema input)
    :confirm_pw (m/validate password-schema input)))