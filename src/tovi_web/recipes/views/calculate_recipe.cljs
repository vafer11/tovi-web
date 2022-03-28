(ns tovi-web.recipes.views.calculate-recipe
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/PictureAsPdf" :default PictureAsPdfIcon]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [tovi-web.components.inputs :refer [text-field]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]))

(defn calculate-recipe-dialog []
  (let [show-dialog? @(subscribe [:show-dialog? :calculate-recipe])
        {id :id ingredients :ingredients} @(subscribe [::subs/active-dialog-recipe :calculate-recipe])
        total-dough-weight (subscribe [::subs/recipe-total-dough-weight id])]
    [:> mui/Dialog {:open show-dialog?
                    :onClose #(dispatch [:hide-dialog])
                    :fullWidth true
                    :maxWidth :md}
     [:> mui/DialogTitle [:> mui/Typography "Calculate"]]
     [:> mui/DialogContent
      [:> mui/Grid {:container true :spacing 1}
       [:> mui/Grid {:container true :item true :justifyContent "right" :alignItems "right" :xs 12}
        [:> mui/IconButton {:aria-label "Download PDF"
                            :onClick #(dispatch [::events/Dowload-pdf id])}
         [:> PictureAsPdfIcon]]]
       [:> mui/Grid {:item true :xs 12}
        [text-field
         [:forms :calculate-recipe :dough-weight]
         {:id :dough-weight
          :label "Dough Weight"
          :autoFocus true
          :fullWidth false
          :type :number
          :InputProps {:startAdornment (as-element [:> mui/InputAdornment {:position "start"} "gr"])}}]]
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
             ^{:key k}
             [:> mui/TableRow
              [:> mui/TableCell (str percentage " %")]
              [:> mui/TableCell label]
              (let [quantity-path [:forms :calculate-recipe :ingredients k :quantity]]
                [:> mui/TableCell [text-field
                                   quantity-path
                                   {:variant :standard
                                    :onChange #(do
                                                 (let [value (.. % -target -value)]
                                                   (dispatch [:set-input-value quantity-path value])
                                                   (dispatch [::events/balance-recipe id k value])))
                                    :fullWidth false
                                    :InputProps {:startAdornment (as-element [:> mui/InputAdornment {:position "start"} "gr"])}}]])
              ])]]]]]]
     [:> mui/DialogActions
      [:> mui/Button {:onClick #(dispatch [:hide-dialog])} "Ok"]]]))