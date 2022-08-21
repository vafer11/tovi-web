(ns tovi-web.recipes.recipe.views
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/Delete" :default DeleteIcon]
            ["@mui/icons-material/AddBox" :default AddBoxIcon] 
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :refer [as-element]]
            [tovi-web.recipes.recipe.events :as events]
            [tovi-web.recipes.recipe.subs :as subs]
            [tovi-web.recipes.recipe.db :refer [valid-input?]]
            [tovi-web.utils :as utils]))

(defn- read-only-ingredients-table []
  (let [recipe-ingredients @(subscribe [::subs/recipe-ingredients])]
    [:> mui/TableContainer {:component mui/Paper}
     [:> mui/Table
      [:> mui/TableHead
       [:> mui/TableRow
        [:> mui/TableCell "Percentage"]
        [:> mui/TableCell "Ingredients"]
        [:> mui/TableCell "Quantity"]]]
      [:> mui/TableBody
       (for [[k {:keys [percentage name quantity]}] recipe-ingredients]
         ^{:key (str k)}
         [:> mui/TableRow {:key (str k)}
          [:> mui/TableCell (str percentage " %")]
          [:> mui/TableCell (str name)]
          [:> mui/TableCell (str quantity " gr")]])]]]))

(defn- on-percentage-change [id input]
  (let [value (utils/to-float (.. input -target -value))]
    (dispatch [::events/set-percentage id value]) 
    (dispatch [::events/set-quantity id (-> value utils/get-quantity)])
    (when (valid-input? :percentage value)
      (dispatch [::events/dissoc-ingredient-error id :percentage]))))

(defn- on-ingredient-change [ri_id input ingredients]
  (let [new-id (.. input -target -value) 
        new-label (get-in ingredients [new-id :name])]
    (dispatch [::events/set-ingredient-id ri_id new-id])
    (dispatch [::events/set-ingredient-label ri_id new-label])
    ;; Need to be done
    ;; (when (valid-input? :id new-id) (dispatch [::events/dissoc-ingredient-error id :id]))
    ))

(defn- editable-ingredients-table []
  (let [ingredients @(subscribe [::subs/ingredients])
        recipe-ingredients @(subscribe [::subs/recipe-ingredients])]
    [:<>
     [:> mui/IconButton
      {:onClick #(dispatch [::events/add-ingredient-to-recipe])
       :aria-label "Add recipe ingredient"
       :style {:marginLeft :auto}}
      [:> AddBoxIcon]]
     [:> mui/TableContainer {:component mui/Paper}
      [:> mui/Table {:aria-label "Editable table"}
       [:> mui/TableHead
        [:> mui/TableRow
         [:> mui/TableCell "Percentage"]
         [:> mui/TableCell "Ingredients"]
         [:> mui/TableCell "Quantity"]
         [:> mui/TableCell "Actions"]]]
       [:> mui/TableBody
        (for [[ri_id {:keys [i_id quantity]}] recipe-ingredients]
          ^{:key (str ri_id)}
          [:> mui/TableRow {:key (str ri_id)}
           [:> mui/TableCell {:width "30%"} 
            [:> mui/TextField
             {:id :percentage
              :variant :standard
              :margin :none
              :size :small
              :value @(subscribe [::subs/percentage-value ri_id])
              :error @(subscribe [::subs/ingredient-percentage-error? ri_id])
              :helperText @(subscribe [::subs/ingredient-percentage-error-msg ri_id])
              :onChange #(on-percentage-change ri_id %1)
              :fullWidth false
              :InputProps {:endAdornment (as-element [:> mui/InputAdornment {:position "start"} "%"])}}]] 
           
           [:> mui/TableCell {:width "30%"}
            [:> mui/FormControl
             {:fullWidth true
              :variant "standard"}
             [:> mui/Select
              {:id (str "ingredient" i_id)
               :value @(subscribe [::subs/ingredient-value ri_id])
               :autoWidth true
               :size :small
               :onChange #(on-ingredient-change ri_id %1 ingredients)}
              (for [[_ {:keys [id name]}] ingredients]
                ^{:key (str "select-" id)}
                [:> mui/MenuItem {:value id} name])]]]
           
           [:> mui/TableCell {:width "20%"}
            (str quantity " gr")]
           
           [:> mui/TableCell {:width "20%"}
            [:> mui/IconButton
             {:aria-label "Delete recipe ingredient"
              :onClick #(dispatch [::events/remove-ingredient-from-recipe ri_id])
              :style {:marginLeft :auto}}
             [:> DeleteIcon]]]])]]]]))

(defn onChange [id input]
  (let [value (.. input -target -value)]
    (dispatch [::events/set-field-value id value])
    (when (valid-input? id value)
      (dispatch [::events/dissoc-error id]))))

(defn- on-image-change [input]
  (let [files (.from js/Array (.. input -target -files))]
    (dispatch [::events/upload-image files])))

(defn recipe [title mode]
  (let [read-only? (= mode :view)
        src @(subscribe [::subs/image-src])]
    [:> mui/Container {:component :main :maxWidth :md :style {:margin-top 30 :margin-bottom 50}}
     [:> mui/Typography {:component :h2 :variant :h5} title]
     [:form {:noValidate true :autoComplete "off"}
      [:> mui/Grid {:container true :spacing 1}
       [:> mui/Grid {:item true :xs 12}
        [:> mui/TextField
         {:id :name
          :label "Recipe"
          :value @(subscribe [::subs/field-value :name])
          :error @(subscribe [::subs/name-error?])
          :helperText @(subscribe [::subs/name-error-msg])
          :onChange #(onChange :name %1)
          :InputProps {:readOnly read-only?}}]] 

       [:> mui/Grid {:item true :xs 12}
        [:> mui/TextField
         {:id :steps
          :label "Steps"
          :multiline true
          :rows 5
          :value @(subscribe [::subs/field-value :steps])
          :error @(subscribe [::subs/steps-error?])
          :helperText @(subscribe [::subs/steps-error-msg])
          :onChange #(onChange :steps %1)
          :InputProps {:readOnly read-only?}}]] 
       
       [:> mui/Grid {:item true :xs 12}
        [:img {:src src :width "100%"}]
        (when-not read-only?
          [:<>
           [:input {:type :file
                    :id :select-image
                    :accept "image/*"
                    :style {:display :none}
                    :onChange on-image-change}]
           [:label {:htmlFor :select-image}
            [:> mui/Button {:component :span} "Upload Image"]]])]

       [:> mui/Grid {:item true :xs 12}
        (if read-only?
          [read-only-ingredients-table]
          (editable-ingredients-table))]
       (when (not read-only?)
         [:> mui/Grid {:item true :xs 12}
          [:> mui/Button {:style {:margin-top 15}
                          :variant :contained
                          :fullWidth true
                          :onClick #(case mode
                                      :create (dispatch [::events/create-recipe])
                                      :edit (dispatch [::events/edit-recipe]))}
           title]])]]]))