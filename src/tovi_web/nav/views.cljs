(ns tovi-web.nav.views
  (:require
   ["@mui/material" :as mui]
   ["@mui/icons-material/Menu" :default MenuIcon]
   ["@mui/icons-material/BakeryDining" :default BakeryDiningIcon]
   ["@mui/icons-material/Logout" :default LogoutIcon]
   ["@mui/icons-material/SupervisorAccount" :default SupervisorAccountIcon]
   ["@mui/icons-material/ListAlt" :default ListAltIcon] 
   [reagent.core :as r]
   [re-frame.core :refer [dispatch subscribe]]
   [tovi-web.account.signin.subs :as subs]))

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
     [:> mui/ListItemButton {:onClick #(dispatch [:navigate :orders])}
      [:> mui/ListItemIcon
       [:> ListAltIcon]]
      [:> mui/ListItemText {:primary "Orders"}]]]

    [:> mui/ListItem {:key "Admin" :disablePadding true}
     [:> mui/ListItemButton {:onClick #(dispatch [:navigate :admin])}
      [:> mui/ListItemIcon
       [:> SupervisorAccountIcon]]
      [:> mui/ListItemText {:primary "Admin"}]]]

    [:> mui/Divider]

    [:> mui/ListItem {:key "Account" :disablePadding true}
     [:> mui/ListItemButton
      [:> mui/ListItemIcon
       [:> LogoutIcon]]
      [:> mui/ListItemText {:primary "Sign Out"}]]]]])


(defn authenticated2 []
  (let [open? (r/atom false)
        {name :name email :email} @(subscribe [::subs/user-information])
        avatar-src "https://w7.pngwing.com/pngs/340/946/png-transparent-avatar-user-computer-icons-software-developer-avatar-child-face-heroes.png"]
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
                        ;:PaperProps {:sx {:backgroundColor "#2c387e":color "white"}}
                        :open @open?
                        :onClose #(reset! open? false)}
         [:> mui/Toolbar
          [:> mui/Avatar {:alt "Profile image"
                          :src avatar-src
                          ;:sx {:width 24 :height 24}
                          }"AF"]
          [:> mui/Box {:sx {:ml 2}}
           [:> mui/Typography {:variant :subtitle1} name]
           [:> mui/Typography {:variant :caption} email]]]
         [:> mui/Divider]
         [menu open?]]]])))

(defn public []
  [:> mui/AppBar {:position :static}
   [:> mui/Toolbar
    [:> mui/Typography {:variant :h6 :className "nav-title-class"} "My Recipes"]]])

(defn nav2 []
  (let [logged-in @(subscribe [::subs/logged-in?])]
    (if logged-in
      [authenticated2]
      [public])))