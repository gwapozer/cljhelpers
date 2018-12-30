(ns cljhelpers.eval_obj_test
  (:require [clojure.test :refer :all]
            [cljhelpers.eval-obj :as eo]))

(defn test-filter-sort []

  (def data
    [{:id 1 :first-name "John1" :last-name "Doe1" :age "14"}
     {:id 4 :first-name "John2" :last-name "Doe2" :age "54"}
     ;{:id 4 :first-name "John3" :last-name "Doe3" :age "34" :education [{:gpa 3} {:gpa 5}] }
     ;{:id 4 :first-name "John4" :last-name "Doe4" :age "12" :education [{:gpa 1} {:gpa 2}]}
     {:id 4 :first-name "John5" :last-name "Doe5" :age "34" :education {:gpa 1 :school "USV"} }
     {:id 6 :first-name "John3" :last-name "Doe3" :age "34" :education {:gpa 5 :school "USV"} }
     {:id 4 :first-name "John4" :last-name "Doe4" :age "12" :job [{:title "SW"} {:title "data parser"}]}

     {:id 5 :first-name "John5" :last-name "Doe5" :age "24"}])

  (def test0 (eo/filter-obj [(comp #(> %1 3) :id)] data))
  (def test1 (eo/filter-obj [(comp odd? :id) (comp #{"Dow1" "Dow2"} :last-name)] data))
  (def test2 (eo/filter-obj [(comp even? :id) (comp #{"Dow1" "Dow2" "Dow3"} :last-name)] data))

  (def test3 (eo/sort-obj [(fn[x y] (if (> (read-string (:age x)) (read-string (:age y)) ) true false))] data))
  (def test4 (eo/sort-obj [(fn[x y] (if (< (:id x) (:id y) ) true false))] data))
  (def test5 (eo/sort-obj [(fn[x y] (compare (:id y) (:id x)))] data))
  (def test6 (eo/sort-obj [(fn [x y] (compare [(:id y) (:first-name x)] [(:id x) (:first-name y)]))] data))
  (def test7 (eo/update-obj [[[:id] #(+ % 3)]] data))
  (def test8 (eo/map-obj [(fn [x] (:education x))] data))
  (def test9 (eo/filter-obj [(fn [x] (not= x nil))] test8))
  (def test0 (eo/filter-obj [(comp #(vector? %) :education)] data))

  (def test10 (eo/filter-obj [(comp #(if-not (nil? %) true false) :education)] data))
  (def test10 (eo/update-obj [[[:education :gpa] #(+ % 3)]] (into [] test10 )))
  (println test10)
  )

(test-filter-sort)