(ns tovi-web.recipes.views.view-recipe
  (:require ["@mui/material" :as mui]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]))

(defn view-recipe-dialog []
  (let [recipe (subscribe [::subs/dialog-recipe :view])]
    (fn []
      (let [{id :id name :name description :description image :image steps :steps ingredients :ingredients} @recipe]
        [:> mui/Dialog {:open (not (nil? id))
                        :onClose #(dispatch [::events/hide-recipe-dialog])
                        :fullWidth true
                        :maxWidth :md}
         [:> mui/DialogTitle [:> mui/Typography name] [:> mui/Typography description]]
         [:> mui/DialogContent
          [:img {:src image}]
          [:> mui/Typography steps]
          [:> mui/TableContainer {:component mui/Paper}
           [:> mui/Table {:sx {:minWidth 650} :aria-label "view-recipe-ingredients"}
            [:> mui/TableHead
             [:> mui/TableRow
              [:> mui/TableCell "Ingredients"]
              [:> mui/TableCell "Quantity"]]]
            [:> mui/TableBody
             (for [[k {:keys [id name quantity]}] ingredients]
               ^{:key id}
               [:> mui/TableRow
                [:> mui/TableCell name]
                [:> mui/TableCell quantity]])]]]]
         [:> mui/DialogActions
          [:> mui/Button {:onClick #(dispatch [::events/hide-recipe-dialog])} "Ok"]]]))))