(ns tovi-web.components.snackbar.views
  (:require ["@mui/material" :as mui]
            [re-frame.core :refer [subscribe dispatch]]
            [tovi-web.components.snackbar.events :as events]))

(defn snackbar []
  (let [{severity :severity msg :msg open? :open?} @(subscribe [::events/snackbar])]
    [:> mui/Snackbar {:open open? :autoHideDuration 6000 :onClose #(dispatch [::events/close-snackbar])}
     [:> mui/Alert {:onClose #(dispatch [::events/close-snackbar]) :severity severity :sx {:width "100%"}}
      msg]]))