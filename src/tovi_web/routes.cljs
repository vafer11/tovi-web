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
          "create-recipe" :create-recipe
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
  (let [panel (keyword (str (name (:handler route)) "-panel"))]
    (re-frame/dispatch [:set-active-panel panel])))

(defonce history
  (pushy/pushy dispatch parse))

(defn navigate!
  [handler]
  (pushy/set-token! history (url-for handler)))

(defn start!
  []
  (pushy/start! history))

