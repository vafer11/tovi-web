(ns tovi-web.routes
  (:require
   [bidi.bidi :as bidi]
   [pushy.core :as pushy]
   [re-frame.core :as re-frame]))

(def routes
  (atom
    ["/" {"" :home
          "about" :about
          "signup" :signup
          "signin" :signin
          "reset-pw" :reset-pw
          "recipes" :recipes
          "orders" :orders
          "admin" :admin
          "create-recipe" :create-recipe
          "view-recipe" :view-recipe
          "edit-recipe" :edit-recipe
          "calculate-recipe" :calculate-recipe}]))

(defn parse
  [url]
  (bidi/match-route @routes url))

(defn url-for
  [& args]
  (apply bidi/path-for (into [@routes] args)))

(defn dispatch
  [route]
  (let [panel (:handler route)]
    (re-frame/dispatch [:set-active-panel panel])))

(defonce history
  (pushy/pushy dispatch parse))

(defn navigate!
  [handler]
  (pushy/set-token! history (url-for handler)))

(defn start!
  []
  (pushy/start! history))

