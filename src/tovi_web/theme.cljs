(ns tovi-web.theme
  (:require ["@mui/material/styles" :refer [createTheme]]))

(defn tovi-theme []
  (createTheme 
   (clj->js
    {:palette
     {:primary  {:main "#618833"}
      :secondary {:main "#b28900"}}
     :typography
     {:useNextVariants true}})))