(ns tovi-web.recipes.recipes.views
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/ModeEdit" :default ModeEditIcon]
            ["@mui/icons-material/Calculate" :default CalculateIcon]
            ["@mui/icons-material/Delete" :default DeleteIcon]
            [tovi-web.recipes.recipes.events :as events]
            [tovi-web.recipes.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]))


(defn- delete-recipe-dialog []
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

(defn- recipe-card [{:keys [id name description image steps]}]
  [:> mui/Card {:sx {:maxWidth 405}}
   [:> mui/CardHeader {:avatar (as-element [:> mui/Avatar {:sx {:bgcolor "red"}} "AF"])
                       :title name
                       :subheader description}]
   [:> mui/CardMedia {:component :img
                      :height 195
                      :image (:src image)
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
                        :onClick #(dispatch [:show-dialog :delete-recipe id])
                        :style {:marginLeft :auto}}
     [:> DeleteIcon]]]])

(defn recipes []
  (let [recipes (subscribe [::subs/recipes])]
    [:<>
     [delete-recipe-dialog]
     [:> mui/Container {:maxWidth :xl :style {:margin-top 20}}
      [:> mui/Button
       {:style {:margin-top 15}
        :onClick #(dispatch [:navigate :create-recipe])}
       "Add recipe 2"]
      [:> mui/Grid {:container true :spacing 4}
       (for [[k v] @recipes]
         ^{:key (str k "-" v)}
         [:> mui/Grid {:item true :xs 12 :sm 6 :md 4 :xl 3 :align :center}
          [recipe-card v]])]]]))