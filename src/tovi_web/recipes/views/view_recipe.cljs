(ns tovi-web.recipes.views.view-recipe
  (:require ["@mui/material" :as mui]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]))

(defn view-recipe-dialog []
  (let [show-dialog? @(subscribe [:show-dialog? :view-recipe])
        recipe @(subscribe [::subs/active-dialog-recipe :view-recipe])
        {name :name description :description image :image steps :steps ingredients :ingredients} recipe]
    [:> mui/Dialog {:open show-dialog?
                    :onClose #(dispatch [:hide-dialog])
                    :fullWidth true
                    :maxWidth :md}
     [:> mui/DialogTitle
      [:> mui/Typography name]
      [:> mui/Typography description]]
     [:> mui/DialogContent
      [:img {:src (:src image)}]
      [:> mui/Typography steps]
      [:> mui/TableContainer {:component mui/Paper}
       [:> mui/Table {;:sx {:minWidth 310} 
                      :aria-label "View Recipe Ingredients"}
        [:> mui/TableHead
         [:> mui/TableRow
          [:> mui/TableCell "Percentage"]
          [:> mui/TableCell "Ingredients"]
          [:> mui/TableCell "Quantity"]]]
        [:> mui/TableBody
         (for [[_ {:keys [id percentage label quantity unit]}] ingredients]
           ^{:key id}
           [:> mui/TableRow
            [:> mui/TableCell (str percentage " %")]
            [:> mui/TableCell label]
            [:> mui/TableCell (str quantity " " unit)]])]]]]
     [:> mui/DialogActions
      [:> mui/Button {:onClick #(dispatch [:hide-dialog])} "Ok"]]]))