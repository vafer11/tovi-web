(ns tovi-web.components.inputs
  (:require ["@mui/material" :as mui]
            [tovi-web.components.events :as events]
            [re-frame.core :refer [dispatch subscribe]]))

(defn text-field [path {:keys [id] :as input-props}]
  (let [field-path (conj path :values id)
        error-path (conj path :errors id)
        value (subscribe [::events/path-db-value field-path])
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

(defn button [name props]
  [:> mui/Button (merge {:variant :contained
                         :color :primary
                         :fullWidth true}
                        props) name])