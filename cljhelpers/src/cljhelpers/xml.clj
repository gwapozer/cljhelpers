(ns cljhelpers.xml
  (:gen-class)
  )

(println "XML parser")

(def node (atom {:name nil :value :nil}))
(def xml-list {:node node :child-list (atom '(node))})

(def xml-string "<root>data<parent description=\"test\"><child>inner data</child></parent></root>")

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

(defn is-stringify [bool-val chr]
  (if (and (= bool-val false) (= chr (first "\"")))
    true
    (if (and (= bool-val true) (= chr (first "\"")))
      false
      bool-val)
    ))

(defn xml-obj [str head tail stringify elt elt-data]
  (if (> (count (rest str)) 0)
    (do
      (let [chr (first str)
            stringify (is-stringify stringify chr)
            head (if (and (= stringify false) (= head false) (= chr (first "<"))) true (cond (= elt true) false :else head))
            tail (if (and (= stringify false) (= tail false) (= chr (first ">"))) true (cond (= elt true) false :else tail))
            elt (if (and (= head true ) (= tail true) (= elt false)) true (if (and (= head true ) (= tail false)) false elt ) )
            ;elt-data (cond (= elt true) ("sdf") :else elt-data)
            depth 1
            ;cntstring (= stringify (+ cntstring 1))
            ]
        (println "chr:" chr " head: " head " tail: " tail " is-stringify:" stringify " Elt data: " elt-data)
        (xml-obj (rest str) head tail stringify elt elt-data)
        )
      )
    (println (first str))
    )
  )

;(iterate-obj xml-string)
(xml-obj xml-string false false false false nil)
;(xml-reader xml-string)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Welcome to clj helpers!"))
