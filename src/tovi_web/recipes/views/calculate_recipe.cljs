(ns tovi-web.recipes.views.calculate-recipe
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/PictureAsPdf" :default PictureAsPdfIcon]
            [tovi-web.utils :as utils]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [tovi-web.components.inputs :refer [text-field]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]))

(defn calculate-recipe []
  (let [{id :id ingredients :ingredients} @(subscribe [::subs/recipe-form])]
    [:> mui/Container {:component :main :maxWidth :md :style {:margin-top 30 :margin-bottom 50}}
     [:> mui/Typography {:component :h2 :variant :h5} "Calculate recipe"]
     [:form {:noValidate true :autoComplete "off"}
      [:> mui/Grid {:container true :spacing 1}
              
       [:> mui/Grid {:item true :xs 5}
        [text-field
         [:forms :recipe :name]
         {:id :name
          :label "Recipe"
          :required true
          :InputProps {:readOnly true}}]]

       [:> mui/Grid {:item true :xs 5}
        (let [dough-weight-path [:forms :recipe :dough-weight]]
          [text-field
           dough-weight-path
           {:id :dough-weight
            :label "Dough Weight"
            :onChange #(let [value (-> % .-target .-value utils/to-int)]
                         (dispatch [:set-input-value dough-weight-path value])
                         (dispatch [::events/balance-recipe-by-dough-weigth value]))
            :InputProps {:endAdornment (as-element [:> mui/InputAdornment {:position "start"} "gr"])}}])]
       
       [:> mui/Grid {:container true :item true :justifyContent "right" :alignItems "right" :xs 2}
        [:> mui/IconButton {:aria-label "Download PDF"
                            :onClick #(dispatch [::events/download-pdf id])}
         [:> PictureAsPdfIcon]]]

       [:> mui/Grid {:item true :xs 12}
        [:> mui/TableContainer {:component mui/Paper}
         [:> mui/Table {;:sx {:minWidth 310} 
                        :aria-label "view-recipe-ingredients"}
          [:> mui/TableHead
           [:> mui/TableRow
            [:> mui/TableCell "Percentage"]
            [:> mui/TableCell "Ingredients"]
            [:> mui/TableCell "Quantity"]]]
          [:> mui/TableBody
           (for [[k {:keys [percentage label]}] ingredients]
             ^{:key (str k)}
             [:> mui/TableRow 
              [:> mui/TableCell {:width "33%"} 
               (str (:value percentage) " %")]
              [:> mui/TableCell {:width "33%"}
               (:value label)]
              (let [quantity-path [:forms :recipe :ingredients k :quantity]]
                [:> mui/TableCell {:width "33%"}
                 [text-field
                  quantity-path
                  {:variant :standard
                   :margin :none
                   :size :small
                   :onChange #(let [value (-> % .-target .-value utils/to-int)]
                                (dispatch [:set-input-value quantity-path value])
                                (dispatch [::events/balance-recipe id k value])
                                (dispatch [::events/calculate-recipe-dough-weight]))
                   :fullWidth false
                   :InputProps {:endAdornment (as-element [:> mui/InputAdornment {:position "start"} "gr"])}}]])])]]]]]]]))