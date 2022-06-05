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

(defn select [id-path label-path {:keys [label id] :as input-props} items]
  (let [value @(subscribe [:input-value id-path])
        error-msg @(subscribe [:input-error id-path])
        error? @(subscribe [:input-error? id-path])
        props (merge {:labelId (str "select-" id)
                      :value value
                      :onChange #(let [value (.. % -target -value)
                                       label (get-in items [value :label])]
                                   (dispatch [:set-input-value label-path label])
                                   (dispatch [:set-input-value id-path value]))}
                     input-props)]
    [:> mui/FormControl
     {:fullWidth true
      :variant "standard"
      :error error?}
     [:> mui/InputLabel {:id (str "select-" id)} label]
     [:> mui/Select props
      (for [[_ {:keys [value label]}] items]
        ^{:key (str "select-" value)}
        [:> mui/MenuItem {:value value} label])]
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