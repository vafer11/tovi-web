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

(defn select [path {:keys [label] :as props} items]
  (let [value @(subscribe [:input-value path])
        error-msg @(subscribe [:input-error path])
        error? @(subscribe [:input-error? path])]
    [:> mui/FormControl
     {:variant :outlined
      :margin :normal
      :required true
      :error error?}
     [:> mui/InputLabel {:id (str "select-" label)} label]
     [:> mui/Select (merge {:labelId (str "select-" label)
                            :value value
                            :on-change #(dispatch [:set-input-value path (.. % -target -value)])}
                           props)
      (for [{:keys [id label]} items]
        ^{:key id}
        [:> mui/MenuItem {:value id} label])]
     [:> mui/FormHelperText error-msg]]))



(defn upload-image [path]
  (let [image (subscribe [:path-db-value path])]
    [:<>
     [:img {:src (:src @image)
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

;; Not used
(defn autocomplete [path-id path-label {:keys [label variant]} options]
  (let [default-value @(subscribe [:input-value path-label])]
    (fn []
      (let [input-value @(subscribe [:input-value path-label])]
        [:> mui/Autocomplete {:disablePortal true
                              :defaultValue default-value
                              :inputValue input-value
                              :onInputChange (fn [_ e] (dispatch [:set-input-value path-label e]))
                              :options options
                              :onChange (fn [_ e] (when e (dispatch [:set-input-value path-id (.-value e)])))
                              :isOptionEqualToValue (fn [_ _] true)
                              :render-input (fn [^js params]
                                              (set! (.-variant params) variant)
                                              (set! (.-label params) label)
                                              (create-element mui/TextField params))}]))))