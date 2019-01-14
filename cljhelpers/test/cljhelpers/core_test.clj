(ns cljhelpers.core-test
  (:require [entity.id-name :refer :all]
            [enums.statuses :as statuses]
            [enums.enumerator :refer :all]
            [enums.months :refer :all]
            [entity.IdName :refer :all]

            )
  (:import (entity.IdName IdName)))

(defn enum-test []
  (println "Testing enumerator")

  (def test-statuses #{(IdName. 1 "start")
                  (IdName. 2 "stop")
                  (IdName. 3 "neutral")})

  ;(println test-statuses)
  ;(println statuses/statuses)
  (println (GetEnumList months :name ) )
  (println (-> (GetEnumByKey test-statuses :name "stop") :id) )
  (println (GetEnumByKey months :id 3))
  )

(enum-test)


