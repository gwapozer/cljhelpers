(ns cljhelpers.dborm
  (:import (java.util Date)
           (java.sql Time Blob Clob)))

(defrecord Where-Cl [field filter value condition Where-Cl])

(defn- set-data [prep-stmt set-cnt value]
  (cond
    (instance? Boolean value) (doto prep-stmt (.setBoolean set-cnt value))
    (instance? Byte value) (doto prep-stmt (.setByte set-cnt value))
    (instance? Short value) (doto prep-stmt (.setShort set-cnt value))
    (instance? Integer value) (doto prep-stmt (.setInt set-cnt value))
    (instance? Long value) (doto prep-stmt (.setLong set-cnt value))
    (instance? Float value) (doto prep-stmt (.setFloat set-cnt value))
    (instance? Double value) (doto prep-stmt (.setDouble set-cnt value))
    (instance? BigDecimal value) (doto prep-stmt (.setBigDecimal set-cnt value))
    (instance? String value) (doto prep-stmt (.setString set-cnt value))
    (instance? (byte []) value) (doto prep-stmt (.setBytes set-cnt value))
    (instance? Date value) (doto prep-stmt (.setDate set-cnt value))
    (instance? Time value) (doto prep-stmt (.setTime set-cnt value))
    (instance? Blob value) (doto prep-stmt (.setBlob set-cnt value))
    (instance? Clob value) (doto prep-stmt (.setClob set-cnt value))
    :else prep-stmt
    )
  )

(defn- get-data [data-type result i]
  (cond
    (= data-type "BIT") (.getBoolean result i)
    (= data-type "TINYINT") (.getShort result i)
    (= data-type "SMALLINT") (.getShort result i)
    (= data-type "INTEGER") (.getInt result i)
    (= data-type "BIGINT") (.getLong result i)
    (= data-type "FLOAT") (.getFloat result i)
    (= data-type "REAL") (.getFloat result i)
    (= data-type "DOUBLE") (.getDouble result i)
    (= data-type "NUMERIC") (.getBigDecimal result i)
    (= data-type "DECIMAL") (.getBigDecimal result i)
    (= data-type "CHAR") (.getString result i)
    (= data-type "VARCHAR") (.getString result i)
    (= data-type "LONGVARCHAR") (.getString result i)
    (= data-type "DATE") (.getDate result i)
    (= data-type "TIME") (.getTime result i)
    (= data-type "TIMESTAMP") (.getTimeStamp result i)
    (= data-type "BINARY") (.getBytes result i)
    (= data-type "VARBINARY") (.getBytes result i)
    (= data-type "LONGVARBINARY") (.getBytes result i)
    (= data-type "NULL") (.getObject result i)
    (= data-type "OTHER") (.getObject result i)
    (= data-type "JAVA_OBJECT") (.getObject result i)
    (= data-type "DISTINCT") (.getObject result i)
    (= data-type "STRUCT") (.getObject result i)
    (= data-type "ARRAY") (.getObject result i)
    (= data-type "BLOB") (.getBlob result i)
    (= data-type "CLOB") (.getClob result i)
    (= data-type "REF") (.getRef result i)
    (= data-type "DATALINK") (.getObject result i)
    (= data-type "BOOLEAN") (.getBoolean result i)
    (= data-type "ROWID") (.getRowId result i)
    (= data-type "NCHAR") (.getString result i)
    (= data-type "NVARCHAR") (.getString result i)
    (= data-type "LONGNVARCHAR") (.getString result i)
    (= data-type "NCLOB") (.getNClob result i)
    (= data-type "SQLXML") (.getSQLXML result i)
    (= data-type "REF_CURSOR") (.getObject result i)
    (= data-type "TIME_WITH_TIMEZONE") (.getObject result i)
    (= data-type "TIMESTAMP_WITH_TIMEZONE") (.getObject result i)
    :else nil
    )
  )

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
         (set-data prep-stmt set-cnt (:value clause))
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
(defn- make-hash
  "Return data in hash-map"
  [results]
  (loop [i 1 hash-buffer {}]
    (if (<= i (-> results (.getMetaData) (.getColumnCount)))
      (do
        (def hash-data {(read-string (str ":" (-> results (.getMetaData) (.getColumnName i))))
                        (get-data (-> results (.getMetaData) (.getColumnTypeName i)) results i)})
        (recur (inc i) (conj hash-buffer hash-data))
        )
      (do
        (conj hash-buffer hash-data)
        )
      )
    )
  )

(defn- make-entity
  "Return entity from hash-map"
  [entity-name hash-result]
  )

(defn db-get
  "Return the results of the select statement"
  [conn entity whereclauses]
  (def sql (str "select " (get-record-keys entity) " from " (get-record-name entity) (cond (nil? whereclauses) "" :else (str " where " (where-cl whereclauses)))))
  (def prep-stmt (.prepareStatement conn sql))
  (def sql-ps (prep-statement prep-stmt whereclauses))
  (def results (.executeQuery sql-ps))

  (def vec-data (loop [i 0 rs-rlt (.next results) vec-buffer []]
                  (if (< i (.getRow results))
                    (do
                      (def rslt (make-hash results))
                      (recur (inc i) (def rs-rlt (.next results)) (conj vec-buffer rslt))
                      )
                    (conj vec-buffer rslt)
                    )
                  ))
  (.close conn)
  vec-data
  )
