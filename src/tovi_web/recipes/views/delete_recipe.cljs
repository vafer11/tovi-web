(ns tovi-web.recipes.views.delete-recipe
  (:require ["@mui/material" :as mui]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]))

(defn delete-recipe-dialog []
  (let [recipe (subscribe [::subs/dialog-recipe :delete])]
    (fn []
      (let [id (:id @recipe)]
        [:> mui/Dialog {:open (not (nil? id))
                        :onClose #(dispatch [::events/hide-recipe-dialog])}
         [:> mui/DialogTitle "Delete recipe"]
         [:> mui/DialogContent
          [:> mui/Typography "Are you sure you want to delete this recipe?"]]
         [:> mui/DialogActions
          [:> mui/Button {:onClick #(dispatch [::events/hide-recipe-dialog])} "Cancel"]
          [:> mui/Button {:onClick #(dispatch [::events/delete-recipe id])} "Delete"]]]))))