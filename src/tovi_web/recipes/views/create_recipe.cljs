(ns tovi-web.recipes.views.create-recipe
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/Delete" :default DeleteIcon]
            ["@mui/icons-material/AddBox" :default AddBoxIcon]
            [tovi-web.components.inputs :refer [text-field autocomplete button]]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]))

(defn- upload-image []
  (let [image-url (subscribe [::subs/recipe-image-url])]
    (fn []
      [:div
       [:img {:src @image-url :width "100%"}]
       [:input {:type :file
                :id :select-image
                :accept "image/*"
                :style {:display :none}
                :onChange #(let [files (.from js/Array (.. % -target -files))]
                             (dispatch [::events/upload-recipe-image files]))}]
       [:label {:htmlFor :select-image}
        [:> mui/Button {:component :span} "Upload Image"]]])))

(defn- editable-ingredients-table []
  (let [ingredients (subscribe [::subs/ingredients])
        recipe-ingredients (subscribe [::subs/recipe-ingredients])]
    (fn []
      [:<>
       [:> mui/IconButton
        {:aria-label "Delete recipe ingredient" :onClick #(dispatch [::events/add-ingredient-recipe]) :style {:marginLeft :auto}}
        [:> AddBoxIcon]]
       [:> mui/TableContainer {:component mui/Paper}
        [:> mui/Table {:sx {:minWidth 450} :aria-label "Editable table"}
         [:> mui/TableHead
          [:> mui/TableRow
           [:> mui/TableCell "Ingredients"]
           [:> mui/TableCell "Quantity"]
           [:> mui/TableCell "Actions"]]]
         [:> mui/TableBody
          (for [[k {:keys [unit]}] (vec @recipe-ingredients)]
            ^{:key k}
            [:> mui/TableRow
             [:> mui/TableCell [autocomplete
                                [:forms :recipe :values :ingredients k]
                                [:forms :recipe :values :ingredients k :label]
                                {:id (str k) :label "" :variant "standard"}
                                ingredients]]
             [:> mui/TableCell [text-field
                                [:forms :recipe :values :ingredients k :quantity]
                                [:forms :recipe :errors :ingredients k :quantity]
                                {:id (str k) :variant :standard :fullWidth false :InputProps {:startAdornment (as-element [:> mui/InputAdornment {:position "start"} unit])}}]]
             [:> mui/TableCell [:> mui/IconButton
                                {:aria-label "Delete recipe ingredient"
                                 :onClick #(dispatch [::events/remove-ingredient-from-recipe k])
                                 :style {:marginLeft :auto}}
                                [:> DeleteIcon]]]])]]]])))

(defn recipe []
  (let [db (subscribe [::subs/get-db])]
    [:> mui/Container {:component :main :maxWidth :md :style {:margin-top 30
                                                              :margin-bottom 50}}
     (-> @db :forms :recipe :values)
     [:> mui/Typography {:component :h2 :variant :h4} "Create new recipe"]
     [:form {:noValidate true :autoComplete "off"}
      [text-field
       [:forms :recipe :values :name]
       [:forms :recipe :errors :name]
       {:id :name :label "Recipe" :required true :autoFocus true}]
      [text-field
       [:forms :recipe :values :description]
       [:forms :recipe :errors :description]
       {:id :description :label "Description" :required true}]
      [text-field
       [:forms :recipe :values :steps]
       [:forms :recipe :errors :steps]
       {:id :steps :label "Steps" :multiline true :rows 5 :required true}]
      [upload-image [:forms :recipe :values :image]]
      [editable-ingredients-table]
      [button "Create recipe" {:onClick #(dispatch [::events/create-recipe])}]]]))