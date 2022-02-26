(ns tovi-web.db)

(def default-db
  {:name "re-frame"
   :forms {:signin {:values {:email "" :password ""}
                    :errors {:email "" :password ""}}
           :signup {:values {:first-name "" :last-name "" :email "" :password "" :confirm-password ""}
                    :errors {:first-name "Required" :last-name "Required" :email "Required" :password "Required" :confirm-password "Required"}}}})
