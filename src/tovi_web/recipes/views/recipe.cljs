(ns tovi-web.recipes.views.recipe
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/Delete" :default DeleteIcon]
            ["@mui/icons-material/AddBox" :default AddBoxIcon]
            [tovi-web.utils :as utils]
            [tovi-web.components.inputs :refer [text-field autocomplete button upload-image]]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]))

(defn- editable-ingredients-table []
  (let [ingredients @(subscribe [::subs/ingredients])
        recipe-ingredients @(subscribe [::subs/form-recipe-ingredients])]
    [:<>
     [:> mui/IconButton
      {:onClick #(dispatch [::events/add-ingredient-to-recipe])
       :aria-label "Add recipe ingredient"  
       :style {:marginLeft :auto}}
      [:> AddBoxIcon]]
     [:> mui/TableContainer {:component mui/Paper}
      [:> mui/Table {;:sx {:minWidth 450} 
                     :aria-label "Editable table"}
       [:> mui/TableHead
        [:> mui/TableRow
         [:> mui/TableCell "Percentage"]
         [:> mui/TableCell "Ingredients"]
         [:> mui/TableCell "Quantity"]
         [:> mui/TableCell "Actions"]]]
       [:> mui/TableBody
        (for [[k {:keys [quantity unit]}] recipe-ingredients]
          ^{:key k}
          [:> mui/TableRow
           (let [percentage-path [:forms :recipe :ingredients k :percentage]
                 quantity-path [:forms :recipe :ingredients k :quantity]]
             [:<>
              [:> mui/TableCell [text-field
                                 percentage-path
                                 {:variant :standard
                                  :fullWidth false
                                  :onChange #(do
                                              (dispatch [:set-input-value percentage-path (.. % -target -value)])
                                              (dispatch [:set-path-db-value quantity-path (-> % .-target .-value utils/get-quantity)]))
                                  :InputProps {:startAdornment (as-element [:> mui/InputAdornment {:position "start"} "%"])}}]]
              [:> mui/TableCell [autocomplete
                                 [:forms :recipe :ingredients k :id]
                                 [:forms :recipe :ingredients k :label]
                                 {:label "" 
                                  :variant "standard"}
                                 ingredients]]
              [:> mui/TableCell (str quantity " " unit)]])
           
           [:> mui/TableCell [:> mui/IconButton
                              {:aria-label "Delete recipe ingredient"
                               :onClick #(dispatch [::events/remove-ingredient-from-recipe k])
                               :style {:marginLeft :auto}}
                              [:> DeleteIcon]]]])]]]]))

(defn recipe [title action]
  (let [db (subscribe [::subs/get-db])]
    [:> mui/Container {:component :main :maxWidth :md :style {:margin-top 30 :margin-bottom 50}}
     (-> @db :forms :recipe :ingredients)

     [:> mui/Typography {:component :h2 :variant :h4} title]
     [:form {:noValidate true :autoComplete "off"}
      [:> mui/Grid {:container true :spacing 1}
       [:> mui/Grid {:item true :xs 12}
        
        [text-field
         [:forms :recipe :name]
         {:id :name :label "Recipe" :required true :autoFocus true}]]
       
       [:> mui/Grid {:item true :xs 12}
        [text-field
         [:forms :recipe :description]
         {:id :description :label "Description" :required true}]]
       
       [:> mui/Grid {:item true :xs 12}
        [text-field
         [:forms :recipe :steps]
         {:id :steps :label "Steps" :multiline true :rows 5 :required true}]]
    
       [:> mui/Grid {:item true :xs 12}
        [upload-image [:forms :recipe :image]]]
       
       [:> mui/Grid {:item true :xs 12}
        [editable-ingredients-table]]
       
       [:> mui/Grid {:item true :xs 12}
        [button title {:style {:margin-top 15}
                       :onClick #(case action
                                  :create (dispatch [::events/create-recipe])
                                  :edit (dispatch [::events/edit-recipe]))}]]]]]))