(ns tovi-web.db
  (:require [re-frame.core :refer [reg-event-db]]))

(def default-db
  {:name "re-frame"
   :snackbar nil
   :account {:id nil
             :name ""
             :last_name ""
             :email ""}
   :forms {:signin {:values {:email ""
                             :password ""}
                    :errors {}
                    :submit-btn {:disabled false
                                 :text "Submit"}}
           :signup {:values {:name ""
                             :last_name ""
                             :email ""
                             :password ""
                             :confirm_pw ""}
                    :errors {}
                    :submit-btn {:disabled false
                                 :text "Submit"}}
           :recipe {:values {:name ""
                             :image nil
                             :steps ""
                             :ingredients {}}
                    :errors {:ingredients {}}}}})

(reg-event-db
 ::initialize-db
 (fn [_ _]
   default-db))
