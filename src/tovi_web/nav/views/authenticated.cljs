(ns tovi-web.nav.views.authenticated
  (:require
   ["@mui/material" :as mui]
   ["@mui/icons-material/Menu" :default MenuIcon]
   ["@mui/icons-material/AccountBox" :default AccountBoxIcon]
   ["@mui/icons-material/Inbox" :default InboxIcon]
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]))

(defn menu [open?]
  [:> mui/Box {:role :presentation
               :onClick #(reset! open? false)
               :onKeyDown #(reset! open? false)}
   [:> mui/List 
    [:> mui/ListItem {:key "Recipes" :disablePadding true}
     [:> mui/ListItemButton {:onClick #(dispatch [:navigate :recipes])}
      [:> mui/ListItemIcon
       [:> InboxIcon]]
      [:> mui/ListItemText {:primary "My Recipes"}]]]
    
    [:> mui/ListItem {:key "Account" :disablePadding true}
     [:> mui/ListItemButton
      [:> mui/ListItemIcon
       [:> AccountBoxIcon]]
      [:> mui/ListItemText {:primary "My Account"}]]]
    
    [:> mui/ListItem {:key "Recipes" :disablePadding true}
     [:> mui/ListItemButton
      [:> mui/ListItemIcon
       [:> AccountBoxIcon]]
      [:> mui/ListItemText {:primary "My Account"}]]]
    
    [:> mui/Divider]

    [:> mui/ListItem {:key "Account" :disablePadding true}
     [:> mui/ListItemButton
      [:> mui/ListItemIcon
       [:> AccountBoxIcon]]
      [:> mui/ListItemText {:primary "My Account"}]]]]])


(defn authenticated []
  (let [open? (r/atom false)]
    (fn []
      [:> mui/AppBar {:position :static}
       [:> mui/Toolbar
        [:> mui/IconButton {:edge :start
                            :color :inherit
                            :aria-label :menu
                            :onClick #(reset! open? true)}
         [:> MenuIcon]]
        [:> mui/Typography {:variant :h6} "My Recipes"]]
       [:div
        [:> mui/Drawer {:anchor "left"
                        :open @open?
                        :onClose #(reset! open? false)}
         [:> mui/Toolbar
          [:> mui/Typography {:variant :h6} "Agustín Fernández"]
          ]
         [:> mui/Divider]
         [menu open?]]]])))