(ns tovi-web.recipes.recipe.db
  (:require [struct.core :as st]))

(defn valid-field? [id input]
  (case id
    :email (st/valid? input :tbd)
    :password (st/valid? input :tbd)))