(ns tovi-web.nav.views.authenticated
  (:require
   ["@mui/material" :as mui]
   ["@mui/icons-material/Menu" :default MenuIcon]
   ["@mui/icons-material/BakeryDining" :default BakeryDiningIcon]
   ["@mui/icons-material/Logout" :default LogoutIcon]
   ["@mui/icons-material/SupervisorAccount" :default SupervisorAccountIcon]
   ["@mui/icons-material/ListAlt" :default ListAltIcon]
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
       [:> BakeryDiningIcon]]
      [:> mui/ListItemText {:primary "My Recipes"}]]]
    
    [:> mui/ListItem {:key "Orders" :disablePadding true}
     [:> mui/ListItemButton
      [:> mui/ListItemIcon
       [:> ListAltIcon]]
      [:> mui/ListItemText {:primary "Orders"}]]]
    
    [:> mui/ListItem {:key "Admin" :disablePadding true}
     [:> mui/ListItemButton
      [:> mui/ListItemIcon
       [:> SupervisorAccountIcon]]
      [:> mui/ListItemText {:primary "Admin"}]]]
    
    [:> mui/Divider]

    [:> mui/ListItem {:key "Account" :disablePadding true}
     [:> mui/ListItemButton
      [:> mui/ListItemIcon
       [:> LogoutIcon]]
      [:> mui/ListItemText {:primary "Sign Out"}]]]]])


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
          [:> mui/Avatar {:alt "Profile image"
                          ;:sx {:width 24 :height 24}
                          } "AF"]
          [:> mui/Typography "Agustín Fernández"]]
         [:> mui/Divider]
         [menu open?]]]])))