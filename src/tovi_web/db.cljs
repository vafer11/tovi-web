(ns tovi-web.db
  (:require [re-frame.core :refer [reg-event-db]]))

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
                                              :percentage 100
                                              :quantity 1000
                                              :unit "gr"}
                                           2 {:id 2
                                              :label "Agua"
                                              :percentage 59
                                              :quantity 590
                                              :unit "ml"}}}}}
   :ingredients {1 {:value 1 :label "Harina"}
                 2 {:value 2 :label "Agua"}
                 3 {:value 3 :label "Sal"}
                 4 {:value 4 :label "Masa madre"}
                 5 {:value 5 :label "Azura"}
                 6 {:value 6 :label "Levadura"}
                 7 {:value 7 :label "Huevos"}
                 8 {:value 8 :label "Grasa"}
                 9 {:value 9 :label "Margarina"}
                 10 {:value 10 :label "Harina integral"}}
   :recipes {1 {:id 1
               :name "Pan Frances"
               :description "Destripción de receta de Pan Frances"
               :image {:name "Imagen Pan Frances"
                       :attachment nil
                       :src ""}
               :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
               :ingredients {1 {:id 1
                                :label "Harina"
                                :percentage 100
                                :quantity 1000
                                :unit "kg"}
                             2 {:id 2
                                :label "Agua"
                                :percentage 60
                                :quantity 500
                                :unit "ml"}
                             3 {:id 3
                                :label "Sal"
                                :percentage 2
                                :quantity 20
                                :unit "gr"}
                             4 {:id 4
                                :label "Levadura"
                                :percentage 1
                                :quantity 10
                                :unit "gr"}}}
             2 {:id 2
                :name "Pan de Viena"
                :description "Destripción de receta de Pan de Viena"
                :image {:name "Imagen Pan Frances"
                        :attachment nil
                        :src ""}
                :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
                :ingredients {1 {:id 1
                                 :label "Harina"
                                 :percentage 100
                                 :quantity 1000
                                 :unit "gr"}
                              2 {:id 2
                                 :label "Agua"
                                 :percentage 60
                                 :quantity 600
                                 :unit "gr"}
                              3 {:id 3
                                 :label "Sal"
                                 :percentage 6
                                 :quantity 60
                                 :unit "gr"}
                              4 {:id 4
                                 :label "Levadura"
                                 :percentage 4
                                 :quantity 40
                                 :unit "gr"}}}
             3 {:id 3
                :name "Bizcochos"
                :description "Destripción de receta de Bizcocho"
                :image {:name "Imagen Pan Frances"
                        :attachment nil
                        :src ""}
                :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
                :ingredients {1 {:id 1
                                 :label "Harina"
                                 :percentage 100
                                 :quantity 1000
                                 :unit "gr"}
                              2 {:id 2
                                 :label "Agua"
                                 :percentage 59
                                 :quantity 590
                                 :unit "gr"}
                              3 {:id 3
                                 :label "Sal"
                                 :percentage 2
                                 :quantity 20
                                 :unit "gr"}
                              4 {:id 4
                                 :label "Levadura"
                                 :percentage 2
                                 :quantity 20
                                 :unit "gr"}}}}})

(reg-event-db
 ::initialize-db
 (fn [_ _]
   default-db))
