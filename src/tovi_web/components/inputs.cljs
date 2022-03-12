(ns tovi-web.components.inputs
  (:require ["@mui/material" :as mui]
            [tovi-web.components.events :as events]
            [reagent.core :refer [create-element]]
            [re-frame.core :refer [dispatch subscribe]]))

(defn text-field [field-path error-path input-props]
  (let [value (subscribe [::events/path-db-value field-path])
        error-msg (subscribe [::events/path-db-value error-path])
        error? (subscribe [::events/field-error? error-path])
        props (merge {:variant :outlined
                      :margin :normal
                      :fullWidth true
                      :value @value
                      :error @error?
                      :helperText @error-msg
                      :onChange #(dispatch [::events/set-path-db-value field-path (.. % -target -value)])}
                     input-props)]
    [:> mui/TextField props]))

(defn autocomplete [field-path label-path {:keys [id label variant]} options]
  (let [inputValue (subscribe [::events/path-db-value label-path])
        value (subscribe [::events/path-db-value field-path])]
    (fn []
      [:> mui/Autocomplete {:disablePortal true
                            :id id
                            :defaultValue @inputValue
                            :inputValue @inputValue
                            :onInputChange (fn [_ e]
                                             (dispatch [::events/set-path-db-value label-path e]))
                            :options @options
                            :onChange (fn [_ e]
                                        (let [new-value (-> @value
                                                            (assoc :id (str (.-value e)))
                                                            (assoc :unit (str (.-unit e))))]
                                          (dispatch [::events/set-path-db-value field-path new-value])))
                            :isOptionEqualToValue (fn [_ _] true)
                            :render-input (fn [^js params]
                                            (set! (.-variant params) variant)
                                            (set! (.-label params) label)
                                            (create-element mui/TextField params))}])))

(defn upload-image [path]
  (let [image (subscribe [::events/image])]
    (fn []
      [:<>
       [:img {:src (:src @image)
              :alt (:name @image)
              :width "100%"}]
       [:input {:type :file
                :id :select-image
                :accept "image/*"
                :style {:display :none}
                :onChange #(let [files (.from js/Array (.. % -target -files))]
                             (dispatch [::events/upload-image path files]))}]
       [:label {:htmlFor :select-image}
        [:> mui/Button {:component :span} "Upload Image"]]])))


(defn button [name props]
  [:> mui/Button (merge {:variant :contained
                         :color :primary
                         :fullWidth true}
                        props) name])