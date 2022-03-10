(ns tovi-web.db)

(def default-db
  {:name "re-frame"
   :forms {:signin {:values {:email "" :password ""}
                    :errors {:email "" :password ""}}
           :signup {:values {:first-name "" :last-name "" :email "" :password "" :confirm-password ""}
                    :errors {:first-name "Required" :last-name "Required" :email "Required" :password "Required" :confirm-password "Required"}}
           :recipe {:values {:name "Recipe name"
                             :description "Recipe desc"
                             :image nil
                             :steps "Steps 1 ... Steps 2 ..."
                             :ingredients {1 {:id 1
                                              :label "Harina"
                                              :quantity 1000
                                              :unit "gr"}
                                           2 {:id 2
                                              :label "Agua"
                                              :quantity 590
                                              :unit "ml"}}}}}
   :ingredients {1 {:value 1 :unit "gr" :label "Harina"}
                 2 {:value 2 :unit "ml" :label "Agua"}
                 3 {:value 3 :unit "gr" :label "Sal"}
                 4 {:value 4 :unit "gr" :label "Masa madre"}
                 5 {:value 5 :unit "gr" :label "Azura"}
                 6 {:value 6 :unit "gr" :label "Levadura"}
                 7 {:value 7 :unit "" :label "Huevos"}
                 8 {:value 8 :unit "gr" :label "Grasa"}
                 9 {:value 9 :unit "gr" :label "Margarina"}
                 10 {:value 10 :unit "gr" :label "Harina integral"}}
   :recipes {1 {:id 1
               :name "Pan Frances"
               :description "Destripción de receta de Pan Frances"
               :image {:name "Imagen Pan Frances"
                       :attachment nil
                       :src "/images/paella.jpg"}
               :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
               :ingredients {1 {:id 1
                                :label "Harina"
                                :name "Harina"
                                :quantity "1000"
                                :unit "kg"}
                             2 {:id 2
                                :label "Agua"
                                :name "Agua"
                                :quantity 500
                                :unit "ml"}
                             3 {:id 3
                                :label "Sal"
                                :name "Sal"
                                :quantity 20
                                :unit "gr"}
                             4 {:id 4
                                :label "Levadura"
                                :name "Levadura"
                                :quantity 4
                                :unit "gr"}}}
             2 {:id 2
                :name "Pan de Viena"
                :description "Destripción de receta de Pan de Viena"
                :image {:name "Imagen Pan Frances"
                        :attachment nil
                        :src "/images/paella.jpg"}
                :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
                :ingredients {1 {:id 1
                                 :name "Harina"
                                 :quantity "1000 kg"}
                              2 {:id 2
                                 :name "Agua"
                                 :quantity "500 ml"}
                              3 {:id 3
                                 :name "Sal"
                                 :quantity "20 gr"}
                              4 {:id 4
                                 :name "Levadura"
                                 :quantity "4 gr"}}}
             3 {:id 3
                :name "Bizcochos"
                :description "Destripción de receta de Bizcocho"
                :image {:name "Imagen Pan Frances"
                        :attachment nil
                        :src "/images/paella.jpg"}
                :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
                :ingredients {1 {:id 1
                                 :name "Harina"
                                 :quantity "1000 kg"}
                              2 {:id 2
                                 :name "Agua"
                                 :quantity "500 ml"}
                              3 {:id 3
                                 :name "Sal"
                                 :quantity "20 gr"}
                              4 {:id 4
                                 :name "Levadura"
                                 :quantity "4 gr"}}}
             4 {:id 4
                :name "Pan de masa madre"
                :description "Destripción de receta de Bizcocho bla bla bla"
                :image {:name "Imagen Pan Frances"
                        :attachment nil
                        :src "/images/paella.jpg"}
                :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
                :ingredients {1 {:id 1
                                 :name "Harina"
                                 :quantity "2500 kg"}
                              2 {:id 2
                                 :name "Agua"
                                 :quantity "545 ml"}
                              3 {:id 3
                                 :name "Sal"
                                 :quantity "27 gr"}
                              4 {:id 4
                                 :name "Levadura"
                                 :quantity "3.6 gr"}}}}})
