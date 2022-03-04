(ns tovi-web.db)

(def default-db
  {:name "re-frame"
   :forms {:signin {:values {:email "" :password ""}
                    :errors {:email "" :password ""}}
           :signup {:values {:first-name "" :last-name "" :email "" :password "" :confirm-password ""}
                    :errors {:first-name "Required" :last-name "Required" :email "Required" :password "Required" :confirm-password "Required"}}}
   :recipes {1 {:id 1
               :name "Pan Frances"
               :description "Destripción de receta de Pan Frances"
               :image "/images/paella.jpg"
               :steps "Steps"
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
             2 {:id 2
                :name "Pan de Viena"
                :description "Destripción de receta de Pan de Viena"
                :image "/images/paella.jpg"
                :steps "Steps"
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
                :image "/images/paella.jpg"
                :steps "Steps"
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
                :image "/images/paella.jpg"
                :steps "Steps"
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
                                 :quantity "4 gr"}}}}})
