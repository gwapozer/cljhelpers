(ns cljhelpers.xml
  (:gen-class)
  )

(println "XML parser")

(def node (atom {:name nil :value :nil}))
(def xml-list {:node node :child-list (atom '(node))})

(def xml-string "<root>data<parent description=\"test\"><child>inner data</child></parent></root>")
(def simple-xml-string "<root><child>inner data</child></root>")

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

(defn xml-obj [str-data node-list curr-node head tail stringify elt-end elt-name elt-data]
  (if (> (count (rest str-data)) 0)
    (do
      (let [chr (first str-data)
            stringify (is-stringify stringify chr)
            head (if (and (= stringify false) (= chr (first "<"))) true (cond (= tail true) false :else head))
            tail (if (and (= stringify false) (= chr (first ">"))) true (cond (= head true) false :else tail))
            elt-start (if (and (= stringify false) (= chr (first "<"))) true false)
            elt-end (if (and (= stringify false) (= chr (first "/"))) true (cond (= head true) false :else elt-end))
            ]

        (if (= elt-start true)
          (do
            (println curr-node)
            (def curr-node {:name nil :data nil :child-list nil})
            )
          curr-node)

        (def -name (if (and (= elt-start false) (= elt-end false) (= head true ) (= tail false) (= stringify false)) (str elt-name chr) (cond (= elt-start true) "" :else elt-name)))
        (def -data (if (= tail true) (str elt-data chr) ""))

        (def updated-node (assoc curr-node :name -name :data -data))

        ;(println "chr:" chr " head: " head " tail: " tail " is-stringify:" stringify " Elt start: " elt-start " Elt End: " elt-end " Elt name: " -name " Elt data: " -data)
        ;(println "Curr node: " updated-node)

        (xml-obj (rest str-data) node-list updated-node head tail stringify elt-end -name -data)
        )
      )
    (println (first str-data))
    )
  )

;(iterate-obj xml-string)
(xml-obj simple-xml-string {} {} false false false false "" "")
;(xml-reader xml-string)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Welcome to clj helpers!"))
