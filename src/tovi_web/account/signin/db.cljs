(ns tovi-web.account.signin.db
  (:require [malli.core :as m]
            [tovi-web.utils :as utils]))

(def email-schema [:re {:error/message "Invalid email"} utils/email-regex])
(def password-schema (utils/text-field 6 16))

(def signin-schema
  [:map
   [:email email-schema]
   [:password password-schema]])

(defn valid-form? [form]
  (m/validate signin-schema form))

(defn validate-form [form]
  (utils/validate-schema signin-schema form))

(defn valid-input? [field-name input]
  (case field-name
    :email (m/validate email-schema input)
    :password (m/validate password-schema input)))
