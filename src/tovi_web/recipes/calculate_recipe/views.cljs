(ns tovi-web.recipes.calculate-recipe.views
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/PictureAsPdf" :default PictureAsPdfIcon]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]
            [tovi-web.recipes.calculate-recipe.events :as events]
            [tovi-web.recipes.calculate-recipe.subs :as subs]
            [tovi-web.utils :as utils]))

(defn- quantity-onChange [recipe-id ingredient-id input]
  (let [value (-> input .-target .-value utils/to-int)]
    (dispatch [::events/set-quantity-value ingredient-id value])
    (dispatch [::events/balance-recipe recipe-id ingredient-id value])
    (dispatch [::events/calculate-recipe-dough-weight])))

(defn- dough-weight-onChange [input]
  (let [value (-> input .-target .-value utils/to-int)]
    (dispatch [::events/set-field-value :dough-weight value])
    (dispatch [::events/balance-recipe-by-dough-weigth value])))

(defn calculate-recipe []
  (let [recipe-id @(subscribe [::subs/recipe-id])
        ingredients @(subscribe [::subs/recipe-ingredients])]
    [:> mui/Container {:component :main :maxWidth :md :style {:margin-top 30 :margin-bottom 50}}
     [:> mui/Typography {:component :h2 :variant :h5} "Calculate recipe"]
     [:form {:noValidate true :autoComplete "off"}
      [:> mui/Grid {:container true :spacing 1}
       [:> mui/Grid {:item true :xs 5}
        [:> mui/TextField
         {:id :name
          :label "Recipe"
          :required false
          :value @(subscribe [::subs/field-value :name])
          :InputProps {:readOnly true}}]]
       [:> mui/Grid {:item true :xs 5}
        [:> mui/TextField
         {:id :dough-weight
          :label "Dough Weight"
          :required false
          :value @(subscribe [::subs/field-value :dough-weight])
          :onChange dough-weight-onChange
          :InputProps {:endAdornment (as-element [:> mui/InputAdornment {:position "start"} "gr"])}}]]

       [:> mui/Grid {:container true :item true :justifyContent "right" :alignItems "right" :xs 2}
        [:> mui/IconButton {:aria-label "Download PDF"
                            :onClick #(dispatch [::events/download-pdf recipe-id])}
         [:> PictureAsPdfIcon]]]

       [:> mui/Grid {:item true :xs 12}
        [:> mui/TableContainer {:component mui/Paper}
         [:> mui/Table {:aria-label "view-recipe-ingredients"}
          [:> mui/TableHead
           [:> mui/TableRow
            [:> mui/TableCell "Percentage"]
            [:> mui/TableCell "Ingredients"]
            [:> mui/TableCell "Quantity"]]]
          [:> mui/TableBody
           (for [[ingredient-id {:keys [percentage label]}] ingredients]
             ^{:key (str ingredient-id)}
             [:> mui/TableRow
              [:> mui/TableCell {:width "33%"}
               (str percentage " %")]
              [:> mui/TableCell {:width "33%"}
               label]
              [:> mui/TableCell {:width "33%"}
               [:> mui/TextField 
                {:id :quantity
                 :variant :standard
                 :margin :none
                 :size :small
                 :value @(subscribe [::subs/quantity-value ingredient-id])
                 :onChange #(quantity-onChange recipe-id ingredient-id %)
                 :fullWidth false
                 :InputProps {:endAdornment (as-element [:> mui/InputAdornment {:position "start"} "gr"])}}]]])]]]]]]]))