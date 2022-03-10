(ns tovi-web.recipes.views.edit-recipe
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/Delete" :default DeleteIcon]
            ["@mui/icons-material/AddBox" :default AddBoxIcon]
            [tovi-web.components.inputs :refer [text-field autocomplete]]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]))

(defn edit-recipe-dialog []
  (let [recipe (subscribe [::subs/dialog-recipe :edit])
        path [:forms :edit-recipe]]
    (fn []
      (let [{id :id image :image ingredients :ingredients} @recipe]
        [:> mui/Dialog {:open (not (nil? id))
                        :onClose #(dispatch [::events/hide-recipe-dialog])
                        :fullWidth true
                        :maxWidth :lg}
         [:> mui/DialogTitle
          [text-field path {:id :name :label "Name" :required true}]
          [text-field path {:id :description :label "Description" :required true}]
          [autocomplete path {:id "id" :label "Ingredients" :variant "standard"}]]
         [:> mui/DialogContent
          [:img {:src image}]
          [text-field path {:id :steps :label "Steps" :multiline true :required true}]
          [:> mui/IconButton {:aria-label "Add recipe ingredient"
                              :onClick #(.log js/console (str "Add ingredients: "))
                              :style {:marginLeft :auto}}
           [:> AddBoxIcon]]
          [:> mui/TableContainer {:component mui/Paper}
           [:> mui/Table {:sx {:minWidth 650} :aria-label "edit-recipe-ingredients"}
            [:> mui/TableHead
             [:> mui/TableRow
              [:> mui/TableCell "Ingredients"]
              [:> mui/TableCell "Quantity"]
              [:> mui/TableCell "Actions"]]]
            [:> mui/TableBody
             (for [[k {:keys [id name quantity]}] ingredients]
               ^{:key id}
               [:> mui/TableRow
                [:> mui/TableCell [:input {:value name}]]
                [:> mui/TableCell [:input {:value quantity}]]
                [:> mui/TableCell
                 [:> mui/IconButton {:aria-label "Delete recipe ingredient"
                                     :onClick #(.log js/console (str "Delete ingredients: " id))
                                     :style {:marginLeft :auto}}
                  [:> DeleteIcon]]]])]]]]
         [:> mui/DialogActions
          [:> mui/Button {:onClick #(dispatch [::events/hide-recipe-dialog])} "Cancel"]
          [:> mui/Button {:onClick #(dispatch [::events/hide-recipe-dialog])} "Update"]]]))))