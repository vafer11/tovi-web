(ns tovi-web.account.signup.db
  (:require [struct.core :as st]))

(def first-name
  {:name
   [[st/required :message "Can´t be blank"]
    [st/string :message "Must be string"]]})

(def last-name
  {:last-name
   [[st/required :message "Can´t be blank"]
    [st/string :message "Must be string"]]})

(def email
  {:email
   [[st/required :message "Can´t be blank"]
    [st/email :message "Invalid email"]]})

(def password
  {:password
   [[st/required :message "Can´t be blank"]
    [st/min-count 6 :message "Must contain at leat six character"]]})

(def confirm-password
  {:confirm-password
   [[st/required :message "Can´t be blank"]
    [st/identical-to :password :message "Does not match"]]})

(def signup-scheme (merge first-name last-name email password confirm-password))

(defn validate-form [form] 
  (st/validate form signup-scheme))

(defn valid-form? [form] 
  (st/valid? form signup-scheme))

(defn valid-field? [id input]
  (case id
    :name (st/valid? input first-name)
    :last-name (st/valid? input last-name)
    :email (st/valid? input email)
    :password (st/valid? input password)
    :confirm-password (st/valid? input confirm-password)))