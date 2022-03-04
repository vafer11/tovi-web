(ns tovi-web.recipes.views.recipes
  (:require ["@mui/material" :as mui]
            ["@mui/icons-material/MoreVert" :default MoreVertIcon]
            [tovi-web.recipes.events :as events]
            [tovi-web.recipes.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as r]))

(defn- recipe-setting-click [state option]
  (swap! state assoc :setting nil))

(defn- recipe [{:keys [id name description image steps]}]
  (let [state (r/atom {})]
    (fn []
      [:<>
       [:> mui/Card {:sx {:maxWidth 425}}
        [:> mui/CardHeader {:avatar (r/as-element [:> mui/Avatar {:sx {:bgcolor "red"}}])
                            :action (r/as-element [:> mui/IconButton {:aria-label :menu
                                                                      :onClick #(swap! state assoc :setting (.-target %))}
                                                   [:> MoreVertIcon]])
                            :title name
                            :subheader description}]
        [:> mui/CardMedia {:component :img
                           :height 194
                           :image image
                           :alt name}]
        [:> mui/CardContent
         [:> mui/Typography {:variant :body2} steps]]]
       [:> mui/Menu {:id "recipe-setting"
                     :anchorEl (:setting @state)
                     :keepMounted true
                     :open (if (:setting @state) true false)
                     :onClose #(swap! state assoc :setting nil)}
        [:> mui/MenuItem {:onClick #(recipe-setting-click state :view)} "View"]
        [:> mui/MenuItem {:onClick #(recipe-setting-click state :edit)} "Edit"]
        [:> mui/MenuItem {:onClick #(recipe-setting-click state :delete)} "Delete"]]])))

(defn recipes []
  (let [recipes (subscribe [::subs/recipes])]
    (fn []
      [:> mui/Container {:maxWidth :xl :style {:margin-top 20}}
       [:> mui/Grid {:container true :spacing 4}
        (for [[k v] @recipes]
          [:> mui/Grid {:item true :xs 12 :md 6 :lg 4 :xl 3 :align :center}
           [recipe v]])]])))

