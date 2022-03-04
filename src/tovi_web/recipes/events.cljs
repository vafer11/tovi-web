(ns tovi-web.recipes.events
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-event-db
 ::recipe-action
 (fn db [_ action]
   (.log js/console (str action))))