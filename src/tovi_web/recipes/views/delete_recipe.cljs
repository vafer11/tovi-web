(ns tovi-web.recipes.views.delete-recipe
  (:require ["@mui/material" :as mui]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]))

(defn delete-recipe-dialog []
  (let [show-dialog? @(subscribe [:show-dialog? :delete-recipe])
        recipe-id @(subscribe [::subs/active-dialog-recipe-id :delete-recipe])]
    [:> mui/Dialog {:open show-dialog?
                    :onClose #(dispatch [:hide-dialog])}
     [:> mui/DialogTitle "Delete recipe"]
     [:> mui/DialogContent
      [:> mui/Typography "Are you sure you want to delete this recipe?"]]
     [:> mui/DialogActions
      [:> mui/Button {:onClick #(dispatch [:hide-dialog])} "Cancel"]
      [:> mui/Button {:onClick #(dispatch [::events/delete-recipe recipe-id])} "Delete"]]]))