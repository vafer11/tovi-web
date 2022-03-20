(ns tovi-web.recipes.views.recipes
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/ModeEdit" :default ModeEditIcon]
            ["@mui/icons-material/Calculate" :default CalculateIcon]
            ["@mui/icons-material/Delete" :default DeleteIcon]
            [tovi-web.recipes.views.view-recipe :refer [view-recipe-dialog]]
            [tovi-web.recipes.views.calculate-recipe :refer [calculate-recipe-dialog]]
            [tovi-web.recipes.views.delete-recipe :refer [delete-recipe-dialog]]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]))

(defn- recipe [{:keys [id name description image steps]}]
  [:> mui/Card {:sx {:maxWidth 425}}
   [:> mui/CardHeader {:avatar (as-element [:> mui/Avatar {:sx {:bgcolor "red"}} "AF"])
                       :title name
                       :subheader description}]
   [:> mui/CardMedia {:component :img
                      :height 195
                      :image (:src image)
                      :alt name
                      :onClick #(dispatch [:show-dialog :view-recipe id])}]
   [:> mui/CardContent
    [:> mui/Typography {:variant :body2} steps]]
   [:> mui/CardActions {:disableSpacing true}
    [:> mui/IconButton {:aria-label "Edit recipe"
                        :onClick #(dispatch [::events/show-edit-recipe id])}
     [:> ModeEditIcon]]
    [:> mui/IconButton {:aria-label "Calculate recipe"
                        :onClick #(dispatch [:show-dialog :calculate-recipe id])}
     [:> CalculateIcon]]
    [:> mui/IconButton {:aria-label "Delete recipe"
                        :onClick #(dispatch [:show-dialog :delete-recipe id])
                        :style {:marginLeft :auto}}
     [:> DeleteIcon]]]])

(defn recipes []
  (let [recipes (subscribe [::subs/recipes])
        db (subscribe [::subs/get-db])]
    (fn []
      [:<>
       [view-recipe-dialog]
       [calculate-recipe-dialog]
       [delete-recipe-dialog]
       (-> @db :active-dialog)
       [:> mui/Container {:maxWidth :xl :style {:margin-top 20}}
        [:> mui/Button
         {:style {:margin-top 15}
          :onClick #(dispatch [:navigate :create-recipe])}
         "Add recipe"]
        [:> mui/Grid {:container true :spacing 4}
         (for [[k v] @recipes]
           ^{:key (str k "-" v)}
           [:> mui/Grid {:item true :xs 12 :md 6 :lg 4 :xl 3 :align :center}
            [recipe v]])]]])))

