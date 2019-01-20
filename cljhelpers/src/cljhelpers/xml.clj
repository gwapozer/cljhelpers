(ns cljhelpers.xml
  (:gen-class)
  (:require [enums.enumerator :refer :all]
            [enums.xml-unit :refer :all])
  )

(println "XML parser")

(defrecord node [name element attribute depth])
(defrecord attribute [name description])
(defrecord data-eval [stringify head tail proc-state])

;(def node (atom {:name nil :value nil :depth nil}))
;(def xml-list {:node node :child-list (atom '(node))})

(def xml-string "<root>data<parent description=\"test\"><child>inner data</child></parent></root>")
(def simple-xml-string "<root age=\"32\" ><child><child2 test=\"data\" /><innerchild item=\"das\" description=\"somedata\">inner data</innerchild><child3>dat</child3></child><additional>sfsfs</additional></root>")

(defn xml-reader
  [xmlstr]
  (def curr-arr (into-array [xmlstr]))
  (loop [i 0]
    (if (< i (count curr-arr))
      (do (println (nth curr-arr i)))
          (recur (inc i))
          )
      )
  )

(defn iterate-obj [s]
  (if (> (count (rest s)) 0)
    (do
      (println "chr:" (first s) )
      (iterate-obj (rest s))
      )
    (println (first s))
    )
  )

(defn- get-next-val
  "Find the next data that is not nil"
  [str-data]
  (cond (and (nil? (first str-data)) (> (count (rest str-data)) 0)) (recur (next str-data))
        :else (first str-data))
  )
(defn is-stringify [bool-val chr]
  (if (and (= bool-val false) (= chr (first "\"")))
    true
    (if (and (= bool-val true) (= chr (first "\"")))
      false
      bool-val)
    ))

(defn xml-obj [str-data node-list node data-eval]
  (if (> (count (rest str-data)) 0)
    (do
      (let [chr (first str-data)
            stringify (is-stringify (-> data-eval :stringify) chr)
            elt-start (if (and (= (-> data-eval :stringify) false) (= chr (first "<")) (not= (get-next-val (rest str-data))(first "/"))) true false)
            elt-end (if (and (= (-> data-eval :stringify) false) (or (and (= chr (first "<")) (= (get-next-val (rest str-data))(first "/"))) (and (= chr (first "/")) (= (get-next-val (rest str-data))(first ">"))))) true false)
            head (if (and (= (-> data-eval :stringify) false) (= chr (first "<")) (not= (get-next-val (rest str-data))(first "/"))) true (cond (or (= elt-end true) (= (-> data-eval :tail) true))  false :else (-> data-eval :head)))
            tail (if (and (= (-> data-eval :stringify) false) (= chr (first ">"))) true (cond (= head true) false :else (-> data-eval :tail)))]

        (def curr-node node)
        (def curr-node-list node-list)

        (if (and (not= (-> curr-node ) nil) (not= (-> data-eval :proc-state) nil) (or (= elt-start true) (= elt-end true)) )
          (do
            ;(println (-> curr-node ))
            (def curr-node-list (conj node-list curr-node))

            (def curr-node (node. "" "" "" (-> curr-node :depth)))
            )
          )

        (let [curr-depth (cond (true? elt-start) (inc (-> curr-node :depth) ) (true? elt-end)  (dec (-> curr-node :depth)) :else (-> curr-node :depth))
              proc-data (cond
                          (true? elt-start) nil
                          (true? elt-end) nil
                          (and (true? head) (true? tail)) "donothing"
                          (and (true? head) (clojure.string/blank? (-> curr-node :name))) (-> (GetEnumByKey xml-unit :name "name") :name)
                          (and (not= (-> data-eval :proc-state) nil) (= (-> data-eval :proc-state) "name") (clojure.string/blank? (str chr)))  (-> (GetEnumByKey xml-unit :name "attribute") :name)
                          (and (not= (-> data-eval :proc-state) nil) (false? head) ) (-> (GetEnumByKey xml-unit :name "element") :name)
                          :else (-> data-eval :proc-state))]

          (def updated-data-eval (assoc data-eval :stringify stringify :head head :tail tail :proc-state proc-data ))
          (def curr-node (assoc curr-node :depth curr-depth))

          (if (not= (-> updated-data-eval :proc-state) nil)
            (do
              (if (= (-> updated-data-eval :proc-state) (-> (GetEnumByKey xml-unit :name "name") :name))
                (do
                  (def -name (str (-> curr-node :name) chr))
                  (def curr-node (assoc curr-node :name -name :depth curr-depth))
                  )
                )

              (if (= (-> updated-data-eval :proc-state) (-> (GetEnumByKey xml-unit :name "attribute") :name))
                (do
                  (def -attribute (str (-> curr-node :attribute) chr))
                  (def curr-node (assoc curr-node :attribute -attribute :depth curr-depth))
                  )
                )

              (if (= (-> updated-data-eval :proc-state) (-> (GetEnumByKey xml-unit :name "element") :name))
                (do
                  (def -data (str (-> curr-node :element) chr))
                  (def curr-node (assoc curr-node :element -data :depth curr-depth))
                  )
                )
              )
            )
          )
        (recur (rest str-data) curr-node-list curr-node updated-data-eval)
        )
      )
    node-list
    )
  )

;(iterate-obj xml-string)
(println (xml-obj simple-xml-string [] (node. "" "" "" -1) (data-eval. false false false nil )))
;(xml-reader xml-string)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Welcome to clj helpers!"))
