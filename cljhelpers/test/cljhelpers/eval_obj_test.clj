(ns cljhelpers.eval_obj_test
  (:require [clojure.test :refer :all]
            [cljhelpers.eval-obj :as eo]))

(defn test-filter-sort []

  (def data
    [{:id 1 :first-name "John1" :last-name "Doe1" :age "14"}
     {:id 4 :first-name "John2" :last-name "Doe2" :age "54"}
     {:id 4 :first-name "John3" :last-name "Doe3" :age "34"}
     {:id 4 :first-name "John4" :last-name "Doe4" :age "12"}
     {:id 5 :first-name "John5" :last-name "Doe5" :age "24"}])

  (def test0 (eo/filter-obj [(comp #(> %1 3) :id)] data))
  (def test1 (eo/filter-obj [(comp odd? :id) (comp #{"Dow1" "Dow2"} :last-name)] data))
  (def test2 (eo/filter-obj [(comp even? :id) (comp #{"Dow1" "Dow2" "Dow3"} :last-name)] data))

  (def test3 (eo/sort-obj [(fn[x y] (if (> (read-string (:age x)) (read-string (:age y)) ) true false))] data))
  (def test4 (eo/sort-obj [(fn[x y] (if (< (:id x) (:id y) ) true false))] data))
  (def test5 (eo/sort-obj [(fn[x y] (compare (:id y) (:id x)))] data))
  (def test6 (eo/sort-obj [(fn [x y] (compare [(:id y) (:first-name x)] [(:id x) (:first-name y)]))] data))

  (println test6)

  )
(test-filter-sort)