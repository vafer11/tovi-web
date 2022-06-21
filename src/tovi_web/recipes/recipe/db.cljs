(ns tovi-web.recipes.recipe.db
  (:require [struct.core :as st]
            [malli.core :as m]
            [malli.error :as me]))

(def recipe-name
  {:name
   [[st/required :message "Can´t be blank"]
    [st/string :message "Must be string"]]})

(def steps
  {:steps
   [[st/required :message "Can´t be blank"]
    [st/string :message "Must be string"]]})

(def percentage
  {:percentage
   [[st/required :message "Can´t be blank"]
    [st/number-str :message "Must be a valid number"]]})

(def recipe-scheme (merge recipe-name steps))


(defn- validate-ingredients [{:keys [ingredients]}]
  (reduce
   (fn [acc [id input]]
     (when-not (st/valid? input percentage)
       (let [error-msg (-> input (st/validate percentage) first)]
         (assoc acc id error-msg))))
   {}
   (seq ingredients)))

(defn- valid-ingredients? [{:keys [ingredients]}]
  (not (some #(not (st/valid? %1 percentage)) (vals ingredients))))

(defn validate-form [form]
  (merge
   (first (st/validate form recipe-scheme))
   {:ingredients (validate-ingredients form)}))

(defn valid-form? [form]
  (and
   (st/valid? form recipe-scheme)
   (valid-ingredients? form)))

(defn valid-field? [id input]
  (case id
    :name (st/valid? input recipe-name)
    :steps (st/valid? input steps)))