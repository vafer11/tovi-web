(ns tovi-web.theme
  (:require ["@mui/material/styles" :refer [createTheme]]))

(defn tovi-theme []
  (createTheme 
   (clj->js
    {:palette
     {:background {:default "#fafafa"}
      :primary  {:main "#121858"}
      :secondary {:main "#009688"}}
     :typography
     {:useNextVariants true
      :h5 {:color "#121858"}}})))