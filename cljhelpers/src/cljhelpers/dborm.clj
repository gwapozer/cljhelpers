(ns cljhelpers.dborm)

(defrecord Where-Cl [field filter value condition Where-Cl])

(defn- get-string-to-delim [strgs delim]
  (loop [i 0 my-string ""]
    (if (not= (str (nth strgs i)) delim)
      (do
        (recur (inc i) (str my-string (nth strgs i)))
        )
      (do
        (str my-string)
        )
      )
    )
  )

(defn- get-record-keys [record]

  (def key-list (map #(str %)(keys record)))

  (loop [i 0 my-string ""]
    (if (< i (count key-list))
      (do
        (recur (inc i) (str my-string (cond (= i 0) "" :else ", ") (reduce str (rest (nth key-list i)) ) ))
        )
      (do
        (str my-string)
        )
      )
    )
  )

(defn- get-record-name [record]

  (def str-data (prn-str record))
  (def str-mod (get-string-to-delim str-data "{"))
  (def str-last (get-string-to-delim (reduce str (reverse str-mod))  "."))

  (reduce str (reverse str-last))
  )

(defn- where-cl [clauses]
  (loop [i 0 sql-str ""]
    (if (< i (count clauses))
      (do
        (def clause (nth clauses i))
        (def sql  (str
                        (cond (nil? (:field clause)) "" :else (str " " (:field clause) " " (:filter clause) " " "?"))
                        (cond (nil? (:condition clause)) "" :else (str " " (:condition clause) " "))
                        (cond (nil? (:Where-Cl clause)) "" :else (str " (" (where-cl (:Where-Cl clause)) ") "))
                        ))
        (recur (inc i) (str sql-str sql))
        )
      sql-str
      )
    )
  )

(defn- prep-statement
  ([prep-stmt clauses]
   (prep-statement prep-stmt 1 clauses))
  ([prep-stmt x clauses]
   (loop [i 0 set-cnt x]
     (if (< i (count clauses))
       (do
         (def clause (nth clauses i))
         (doto prep-stmt (.setInt set-cnt (:value clause)))
         (if-not (nil? (:Where-Cl clause))
           (prep-statement prep-stmt (+ set-cnt 1) (:Where-Cl clause))
           )
         (recur (inc i) (inc set-cnt))
         )
       prep-stmt
       )
     )
    )
  )

(defn db-get [conn entity fields whereclauses]
  (def sql (str "select " (get-record-keys entity) " from " (get-record-name entity) (cond (nil? whereclauses) "" :else (str " where " (where-cl whereclauses)))))
  (def prep-stmt (.prepareStatement conn sql))
  (def sql-ps (prep-statement prep-stmt whereclauses))
  (def results (.executeQuery sql-ps))
  (.close conn)
  )
