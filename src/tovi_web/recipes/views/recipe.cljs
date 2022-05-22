(ns tovi-web.recipes.views.recipe
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/Delete" :default DeleteIcon]
            ["@mui/icons-material/AddBox" :default AddBoxIcon]
            [tovi-web.utils :as utils]
            [tovi-web.components.inputs :refer [text-field select autocomplete button upload-image]]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]))

(defn- editable-ingredients-table [read-only?]
  (let [ingredients @(subscribe [::subs/ingredients])
        recipe-ingredients @(subscribe [::subs/form-recipe-ingredients])]
    [:<>
     (when (not read-only?)
       [:> mui/IconButton
        {:onClick #(dispatch [::events/add-ingredient-to-recipe])
         :aria-label "Add recipe ingredient"
         :style {:marginLeft :auto}}
        [:> AddBoxIcon]])
     [:> mui/TableContainer {:component mui/Paper}
      [:> mui/Table {;:sx {:minWidth 450} 
                     :aria-label "Editable table"}
       [:> mui/TableHead
        [:> mui/TableRow
         [:> mui/TableCell "Percentage"]
         [:> mui/TableCell "Ingredients"]
         [:> mui/TableCell "Quantity"]
         (when (not read-only?) 
           [:> mui/TableCell "Actions"])]]
       [:> mui/TableBody
        (for [[k {:keys [quantity unit]}] recipe-ingredients]
          ^{:key k}
          (let [percentage-path [:forms :recipe :ingredients k :percentage]
                quantity-path [:forms :recipe :ingredients k :quantity]]
            [:> mui/TableRow
             [:> mui/TableCell {:width "30%"}
              [text-field
               percentage-path
               {:variant :standard
                :fullWidth true
                :onChange #(do
                             (dispatch [:set-input-value percentage-path (.. % -target -value)])
                             (dispatch [:set-input-value quantity-path (-> % .-target .-value utils/get-quantity)]))
                :InputProps {:readOnly read-only?
                             :startAdornment (as-element [:> mui/InputAdornment {:position "start"} "%"])}}]]
             [:> mui/TableCell {:width "45%"}
              (comment [autocomplete
                        [:forms :recipe :ingredients k :id]
                        [:forms :recipe :ingredients k :label]
                        {:label ""
                         :variant "standard"
                         :InputProps {:readOnly read-only?}}
                        ingredients])
              [autocomplete
               [:forms :recipe :ingredients k :id]
               [:forms :recipe :ingredients k :label]
               {:label ""
                :variant "standard"
                :InputProps {:readOnly read-only?}}
               ingredients]
              (comment [select
                        [:forms :recipe :ingredients k :id]
                        {:label "Ingredient"
                         :variant "standard"
                         :InputProps {:readOnly read-only?}}
                        ingredients])
              ]
             [:> mui/TableCell {:width "15%"}
              (str (:value quantity) " " unit)]
             (when (not read-only?)
               [:> mui/TableCell {:width "10%"}
                [:> mui/IconButton
                 {:aria-label "Delete recipe ingredient"
                  :onClick #(dispatch [::events/remove-ingredient-from-recipe k])
                  :style {:marginLeft :auto}}
                 [:> DeleteIcon]]])]))]]]]))

(defn recipe [title mode]
  (let [read-only? (= mode :view)
        src @(subscribe [::subs/recipe-image])]
    [:> mui/Container {:component :main :maxWidth :md :style {:margin-top 30 :margin-bottom 50}}
     [:> mui/Typography {:component :h2 :variant :h4} title]
     [:form {:noValidate true :autoComplete "off"}
      [:> mui/Grid {:container true :spacing 1}
       [:> mui/Grid {:item true :xs 12}
        [text-field
         [:forms :recipe :name]
         {:id :name 
          :label "Recipe" 
          :required true 
          :InputProps {:readOnly read-only?}}]]

       [:> mui/Grid {:item true :xs 12}
        [text-field
         [:forms :recipe :description]
         {:id :description 
          :label "Description" 
          :required true
          :InputProps {:readOnly read-only?}}]]

       [:> mui/Grid {:item true :xs 12}
        [text-field
         [:forms :recipe :steps]
         {:id :steps 
          :label "Steps" 
          :multiline true 
          :rows 5 
          :required true
          :InputProps {:readOnly read-only?}}]]

       [:> mui/Grid {:item true :xs 12}
        (if read-only?
          [:img {:src src :width "100%"}]
          [upload-image [:forms :recipe :image]])]

       [:> mui/Grid {:item true :xs 12}
        [editable-ingredients-table read-only?]]

       (when (not read-only?)
         [:> mui/Grid {:item true :xs 12}
          [button title {:style {:margin-top 15}
                         :onClick #(case mode
                                     :create (dispatch [::events/create-recipe])
                                     :edit (dispatch [::events/edit-recipe]))}]])]]]))