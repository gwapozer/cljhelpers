(ns cljhelpers.item-obj-test
  (:require [clojure.test :refer :all]
            [entity.id-name :refer :all]
            [cljhelpers.item_obj :as mapper]
            [cljhelpers.eval-obj :as eo]
            ))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))


(defn build-atom []

  (def item1 (id-name 1 "data1"))
  (def item2 (id-name 2 "data2"))
  (def item3 (id-name 3 "data3"))

  (def item-list (list-add nil item1))
  (def item-list (list-add item-list item2))
  (def item-list (list-add item-list {:id 5 :name "Name1"}))

  (println (str "List data" (:id item-list (first item-list))))

  (def atom1 (mapper/atomizer))
  (mapper/set-atomizer atom1 [])
  (mapper/add-item atom1 item1)
  (mapper/add-item atom1 item2)
  (mapper/add-item atom1 item3)

  (def atom2 (mapper/atomizer))
  (mapper/set-atomizer atom2 [])
  (mapper/add-item atom2 item1)
  (mapper/add-item atom2 item3)

  ;(println @atom1 )
  ;(println @atom2 )

  (println "Eval atom item")
  )

(defn print-atom []
  (build-atom))

(print-atom)


