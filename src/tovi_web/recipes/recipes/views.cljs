(ns tovi-web.recipes.recipes.views
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/ModeEdit" :default ModeEditIcon]
            ["@mui/icons-material/Calculate" :default CalculateIcon]
            ["@mui/icons-material/Delete" :default DeleteIcon]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]
            [tovi-web.recipes.recipes.events :as events]
            [tovi-web.recipes.recipes.subs :as subs]))


(defn- delete-recipe-dialog []
  (let [show-dialog? @(subscribe [::subs/show-delete-dialog?])
        recipe-id @(subscribe [::subs/delete-dialog-recipe-id])]
    [:> mui/Dialog {:open show-dialog?
                    :onClose #(dispatch [::events/hide-delete-dialog])}
     [:> mui/DialogTitle "Delete recipe"]
     [:> mui/DialogContent
      [:> mui/Typography "Are you sure you want to delete this recipe?"]]
     [:> mui/DialogActions
      [:> mui/Button {:onClick #(dispatch [::events/hide-delete-dialog])} "Cancel"]
      [:> mui/Button {:onClick #(dispatch [::events/delete-recipe recipe-id])} "Delete"]]]))

(defn- recipe-card [{:keys [id name image steps]}]
  (let [src (if (:src image) (:src image) "/images/bread.jpg")]
    [:> mui/Card {:sx {:maxWidth 405}}
     [:> mui/CardHeader {:avatar (as-element [:> mui/Avatar {:sx {:bgcolor "red"}} "AF"])
                         :title name}]
     [:> mui/CardMedia {:component :img
                        :height 195
                        :image src
                        :alt name
                        :onClick #(dispatch [::events/show-recipe id :view-recipe])}]
     [:> mui/CardContent
      [:> mui/Typography {:variant :body2} steps]]
     [:> mui/CardActions {:disableSpacing true}
      [:> mui/IconButton {:aria-label "Edit recipe"
                          :onClick #(dispatch [::events/show-recipe id :edit-recipe])}
       [:> ModeEditIcon]]
      [:> mui/IconButton {:aria-label "Calculate recipe"
                          :onClick #(dispatch [::events/show-recipe id :calculate-recipe])}
       [:> CalculateIcon]]
      [:> mui/IconButton {:aria-label "Delete recipe"
                          :onClick #(dispatch [::events/show-delete-dialog id])
                          :style {:marginLeft :auto}}
       [:> DeleteIcon]]]]
    ))

(defn recipes []
  (let [recipes (subscribe [::subs/recipes])]
    [:<>
     [delete-recipe-dialog]
     [:> mui/Container {:maxWidth :xl :style {:margin-top 20
                                              :margin-bottom 20}}
      [:> mui/Button
       {:style {:margin-top 15}
        :fullWidth false
        :margin :normal
        :onClick #(dispatch [:navigate :create-recipe])}
       "Add recipe"]
      [:> mui/Grid {:container true :spacing 4}
       (for [[k v] @recipes]
         ^{:key (str k "-" v)}
         [:> mui/Grid {:item true :xs 12 :sm 6 :md 4 :xl 3 :align :center}
          [recipe-card v]])]]]))