(ns dx-prototype.helpers.xml_reader
  (:require [dx-prototype.enums.enumerator :refer :all]
            [dx-prototype.enums.xml_unit :refer :all]
            [obj-eval.core :as oe])
  )

(defrecord node [id parent_id name element attribute depth])
(defrecord attribute [name description])
(defrecord data-eval [stringify head tail proc-state])

(defn- get-next-val
  "Find the next data that is not nil"
  [str-data]
  (cond (and (nil? (first str-data)) (> (count (rest str-data)) 0)) (recur (next str-data))
        :else (first str-data))
  )

(defn is-stringify
  [bool-val chr]
  (if (and (= bool-val false) (= chr (first "\"")))
    true
    (if (and (= bool-val true) (= chr (first "\"")))
      false
      bool-val)
    ))

(defn- prev-node
  "Return the node based on depth"
  [node-list depth]
  (let [node (filterv #(= (:depth %) depth) node-list)]
    (last node))
  )

(defn- get-xml-attribute-list
  "Build the hash data of an xml attribute"
  [str-data curr-vec key-data buff-data bool-is-string]
  (if (> (count (rest str-data)) 0)
    (let [chr (first str-data)
          tmp-key-data (cond (false? bool-is-string) (str key-data chr))
          tmp-data (cond (true? bool-is-string) (str buff-data chr))
          stringify (is-stringify bool-is-string chr)]
      (recur (rest str-data)
             (cond
               (and (false? stringify) (= chr (first "=")))
               (conj curr-vec {(keyword (clojure.string/trim (str (apply str (butlast tmp-key-data))))) ""})
               (and (false? stringify) (false? (clojure.string/blank? tmp-data)))
               (assoc curr-vec (- (count curr-vec) 1) (assoc-in (last curr-vec) [(first (keys (last curr-vec)))] (apply str (butlast tmp-data))))
               :else curr-vec
               )
             (cond (and (false? stringify) (= chr (first "="))) "" :else tmp-key-data)
             (cond (false? stringify) "" :else tmp-data)
             stringify)
      )
    (into {} (assoc curr-vec (- (count curr-vec) 1) (assoc-in (last curr-vec) [(first (keys (last curr-vec)))] buff-data)))
    )
  )

;TODO make code shorter
(defn xml-obj
  [str-data node-list node data-eval counter]
  (if (> (count (rest str-data)) 0)
    (let [chr (first str-data)
          stringify (is-stringify (-> data-eval :stringify) chr)
          elt-start (if (and (= (-> data-eval :stringify) false) (= chr (first "<")) (not= (get-next-val (rest str-data))(first "/"))) true false)
          elt-end (if (and (= (-> data-eval :stringify) false) (or (and (= chr (first "<")) (= (get-next-val (rest str-data))(first "/"))) (and (= chr (first "/")) (= (get-next-val (rest str-data))(first ">"))))) true false)
          head (if (and (= (-> data-eval :stringify) false) (= chr (first "<")) (not= (get-next-val (rest str-data))(first "/"))) true (cond (or (= elt-end true) (= (-> data-eval :tail) true))  false :else (-> data-eval :head)))
          tail (if (and (= (-> data-eval :stringify) false) (= chr (first ">"))) true (cond (= head true) false :else (-> data-eval :tail)))]

      (def curr-node node)
      (def curr-node-list node-list)

      ;Add node to node-list; instantiate new node for next element
      (if (and (not= (-> curr-node ) nil) (not= (-> data-eval :proc-state) nil) (or (= elt-start true) (= elt-end true)) )
        (let [curr_depth (:depth node)
              last_depth (cond (> (count node-list) 0) (:depth (last node-list)) :else 0)
              parent_id (cond (> last_depth curr_depth) (:parent_id (prev-node curr-node-list curr_depth))
                              (< last_depth curr_depth) (:id (last node-list))
                              :else (:parent_id (last node-list)))
              attribute-map (cond (clojure.string/blank? (:attribute curr-node)) {} :else (get-xml-attribute-list (clojure.string/trim (:attribute curr-node))  [] "" "" false))
              this-curr-node (assoc curr-node :id (str (:name curr-node) counter) :parent_id parent_id :attribute attribute-map)]
          (def curr-node-list (conj node-list this-curr-node))
          (def curr-node (node. nil nil "" "" "" (-> curr-node :depth)))
          )
        )
      ;END
      ;Evaluate node depth and data per xml unit
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
      ;END
      (recur (rest str-data) curr-node-list curr-node updated-data-eval (inc counter))
      )
    node-list
    )
  )

(defn get-node-list-by-id
  "Iterate through xml data for all child of parent"
  [id node-list xml-obj]
  (let [listofnodes (filterv #(= (:parent_id %) id) xml-obj)]
    (loop [i 0 buff-list node-list]
      (if (< i (count listofnodes))
        (let [curr_node (nth listofnodes i)
              updated-list (trampoline get-node-list-by-id (:id curr_node) (conj buff-list curr_node) xml-obj)]
          (recur (inc i) updated-list)
          )
        buff-list
        )
      )
    )
  )