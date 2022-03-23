(ns tovi-web.components.inputs
  (:require ["@mui/material" :as mui]
            [tovi-web.components.events :as events]
            [reagent.core :refer [create-element]]
            [re-frame.core :refer [dispatch subscribe]]))

(defn text-field [path input-props]
  (let [value @(subscribe [:input-value path])
        error-msg @(subscribe [:input-error path])
        error? @(subscribe [:input-error? path])
        props (merge {:variant :outlined
                      :margin :normal
                      :fullWidth true
                      :value value
                      :error error?
                      :helperText error-msg
                      :onChange #(dispatch [:set-input-value path (.. % -target -value)])}
                     input-props)]
    [:> mui/TextField props]))


(defn autocomplete [path-id path-label {:keys [id label variant]} options]
  (let [default-value @(subscribe [:input-value path-label])
        onInputChange-fn (fn [_ e]
                           (dispatch [:set-input-value path-label e]))
        onChange-fn (fn [_ e]
                      (when e
                        (dispatch [:set-input-value path-id (.-value e)])))]
    (fn []
      (let [input-value @(subscribe [:input-value path-label])]
        [:> mui/Autocomplete {:disablePortal true
                              :id id
                              :defaultValue default-value
                              :inputValue input-value
                              :onInputChange onInputChange-fn
                              :options options
                              :onChange onChange-fn
                              :isOptionEqualToValue (fn [_ _] true)
                              :render-input (fn [^js params]
                                              (set! (.-variant params) variant)
                                              (set! (.-label params) label)
                                              (create-element mui/TextField params))}]))))


(defn upload-image [path]
  (let [image (subscribe [:path-db-value path])]
    [:<>
     [:img {:src (:src @image)
            :alt (:name @image)
            :width "100%"}]
     [:input {:type :file
              :id :select-image
              :accept "image/*"
              :style {:display :none}
              :onChange #(let [files (.from js/Array (.. % -target -files))]
                           (dispatch [:upload-image path files]))}]
     [:label {:htmlFor :select-image}
      [:> mui/Button {:component :span} "Upload Image"]]]))


(defn button [name props]
  [:> mui/Button (merge {:variant :contained
                         :color :primary
                         :fullWidth true}
                        props) name])