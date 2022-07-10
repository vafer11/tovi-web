(ns tovi-web.recipes.recipe.db
  (:require [malli.core :as m]
            [tovi-web.utils :as utils]))

(def name-schema (utils/text-field 2 10))
(def steps-schema (utils/text-field 2 900))

(def percentage-schema
  [:fn {:error/fn (fn [{:keys [value]} _]
                    (when-not (and (> value 0) (<= value 100)) 
                      "Must be between 0 and 100"))}
   (fn [x] (and (> x 0) (<= x 100)))])

(def id-schema :int)

(def recipe-schema
  [:map
   [:name name-schema]
   [:steps steps-schema]
   [:ingredients
    [:map-of :int [:map
                   [:id id-schema]
                   [:label :string]
                   [:percentage percentage-schema] 
                   [:quantity :int]]]]])

(defn valid-form? [form]
  (m/validate recipe-schema form))

(defn valid-input? [field-name input]
  (case field-name
    :name (m/validate name-schema input)
    :steps (m/validate steps-schema input)
    :percentage (m/validate percentage-schema input)))

(defn validate-form [form]
  (utils/validate-schema recipe-schema form))