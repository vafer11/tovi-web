(ns tovi-web.account.views.signup
  (:require
   ["@mui/material/Container" :default Container]
   ["@mui/material/Typography" :default Typography]
   ["@mui/material/TextField" :default TextField]
   ;[re-frame.core :as re-frame]
   ;[tovi-web.account.events :as events]
   ;[tovi-web.account.subs :as subs]
   ))

(defn signup []
  (let [path [:forms :signup]]
    [:> Container {:component :main :maxWidth :xs :style {:margin-top 20}}
     [:> Typography {:component :h1 :variant :h5} "Sign Up"]
     [:form {:noValidate true}
      [:> TextField {:id :id :label "Label" :variant :outlined}]]]))
