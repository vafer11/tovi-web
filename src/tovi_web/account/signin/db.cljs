(ns tovi-web.account.signin.db
  (:require [struct.core :as st]))

(def email 
  {:email
   [[st/required :message "Can´t be blank"]
    [st/email :message "Invalid email"]]})

(def password
  {:password
   [[st/required :message "Can´t be blank"]
    [st/min-count 6 :message "Must contain at leat six character"]]})

(def signin-scheme
  (merge email password))

(defn validate-form [values] (st/validate values signin-scheme))
(defn valid-form? [values] (st/valid? values signin-scheme))

(defn valid-field? [id input]
  (case id
    :email (st/valid? input email)
    :password (st/valid? input password)))
