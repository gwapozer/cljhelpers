(ns cljhelpers.xml
  (:gen-class)
  )

(println "XML parser")

(def node (atom {:name nil :value :nil}))
(def xml-list {:node node :child-list (atom '(node))})

(def xml-string "<root><parent><child></child></parent></root>")

(defn xml-reader [xmlstr]

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

(defn xml-obj [s head tail]
  (if (> (count (rest s)) 0)
    (do
      (let [chr (first s)
            tail (= chr (first ">"))
            elt (and (= head true ) (= tail true))
            head (or (and (= tail false) (= head true))(and (= tail false) (= head false) (= chr (first "<")))  )
            depth 1
            ;cntstring (= stringify (+ cntstring 1))
            ]
        (println "chr:" chr " head: " head " tail: " tail)
        (xml-obj (rest s) head tail)
        )
      )
    (println (first s))
    )
  )

(iterate-obj xml-string)
(xml-obj xml-string true true)
;(xml-reader xml-string)

