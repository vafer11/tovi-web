(ns tovi-web.db
  (:require [re-frame.core :refer [reg-event-db]]))


; All Inputs values are stored following this structure {:forms :form-name {:input-name1 {:value input-value
;                                                                                         :error input-error}
;                                                                           :input-name2 {:value input-value
;                                                                                         :error input-error}
;}}
(def default-db
  {:name "re-frame"
   :account {:uid "uid" :name "Agustín Fernández" :email "agufercar@gmail.com"}
   :forms {:signin {:values {:email ""
                             :password ""}
                    :errors {}}
           :signup {:values {:name ""
                             :last-name ""
                             :email ""
                             :password ""
                             :confirm-password ""}
                    :errors {}}
           :recipe {:name {:value "Recipe name" :error nil}
                    :description {:value "Recipe name" :error nil}
                    :image nil
                    :steps {:value "Steps 1 ... Steps 2 ..." :error nil}
                    :ingredients {1 {:id {:value 1 :error nil}
                                     :label {:value "Harina"}
                                     :percentage {:value 100 :error nil}
                                     :quantity {:value 1000}}
                                  2 {:id {:value 2 :error nil}
                                     :label {:value "Agua"}
                                     :percentage {:value 59 :error nil}
                                     :quantity {:value 590}}}}}
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
                :image {:name "Pan Frances" :attachment nil :src ""}
                :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
                :ingredients {1 {:id 1 :label "Harina" :percentage 100 :quantity 1000}
                              2 {:id 2 :label "Agua" :percentage 60 :quantity 500}
                              3 {:id 3 :label "Sal" :percentage 2 :quantity 20}
                              4 {:id 6 :label "Levadura" :percentage 1 :quantity 10}}}
             2 {:id 2
                :name "Pan de Viena"
                :description "Destripción de receta de Pan de Viena"
                :image {:name "Pan de Viena" :attachment nil :src ""}
                :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
                :ingredients {1 {:id 1 :label "Harina" :percentage 100 :quantity 1000}
                              2 {:id 2 :label "Agua" :percentage 60 :quantity 600}
                              3 {:id 8 :label "Grasa" :percentage 6 :quantity 60}
                              4 {:id 9 :label "Margarina" :percentage 4 :quantity 40}}}
             3 {:id 3
                :name "Bizcochos"
                :description "Destripción de receta de Bizcochos"
                :image {:name "Bizcochos" :attachment nil :src ""}
                :steps "Integrar bien todos los ingredients despues bla bla bla bla bla bla bla bla bla"
                :ingredients {1 {:id 1 :label "Harina" :percentage 100 :quantity 1000}
                              2 {:id 10 :label "Harina integral" :percentage 59 :quantity 590}
                              3 {:id 3 :label "Sal" :percentage 2 :quantity 20}
                              4 {:id 6 :label "Levadura" :percentage 2 :quantity 20}}}}})

(reg-event-db
 ::initialize-db
 (fn [_ _]
   default-db))
