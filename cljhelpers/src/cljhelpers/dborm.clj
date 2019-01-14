(ns cljhelpers.dborm
  (:require [clojure.java.jdbc :as jdbc]
            [cljhelpers.fx_obj :as fx_obj]
            [enums.enumerator :as enumobj]
            [cljhelpers.eval-obj :as oe]
            [widget.evalobj-gui :as egui]
           )
  (:import (java.util Date)
           (java.sql Time Blob Clob)))

(defrecord Where-Cl [field filter value condition Where-Cl])
(defrecord obj-val [data-type value])

(defn- get-datatype [value]
  (cond
    (nil? value) nil
    (instance? Boolean value) "BOOLEAN"
    (instance? Byte value) "BYTE"
    (instance? Short value) "SHORT"
    (instance? Integer value) "INTEGER"
    (instance? Long value) "LONG"
    (instance? Float value) "FLOAT"
    (instance? Double value) "DOUBLE"
    (instance? BigDecimal value) "DECIMAL"
    (instance? String value) "STRING"
    (instance? (byte []) value) "BOOLEAN"
    (instance? Date value) "DATE"
    (instance? Time value) "TIME"
    (instance? Blob value) "BLOB"
    (instance? Clob value) "CLOB"
    :else "OBJECT"
    )
  )

(defn- set-data [prep-stmt set-cnt value]
  (cond
    (nil? value) (doto prep-stmt (.setObject set-cnt value))
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

(defn- get-record-name
  [record]
  (def str-data (prn-str record))
  (def str-mod (get-string-to-delim str-data "{"))
  (def str-last (get-string-to-delim (reduce str (reverse str-mod))  "."))
  (reduce str (reverse str-last))
  )

(defn- get-record-keys-list
  ([entity]
   (map #(reduce str (rest (str %)) )(keys entity))
    )
  ([entity-definition entity]
   (let [pks (enumobj/GetEnumListByKey entity-definition :is-pk true)
         pks-value (enumobj/GetEnumValueList pks :name)
         record-keys (get-record-keys-list entity)]
     (remove (into #{} pks-value) record-keys)
     )
    )
  )

(defn- get-record-values-list
  ([entity]
   (def record-keys (get-record-keys-list entity))
   (def new-keys (map #(-> entity (read-string (str ":" %))) record-keys))
   (zipmap new-keys (map #(-> entity %) new-keys))
    ;(map #(-> %)(vals entity))
    )

  ([entity-definition entity]
   (def record-keys (get-record-keys-list entity-definition entity ))
   (def new-keys (map #(-> entity (read-string (str ":" %))) record-keys))
   (zipmap new-keys (map #(-> entity %) new-keys))
    ;(map #(-> entity %) new-keys)
    )
  )

(defn- get-record-values-objdata-list
  ([val-list]
   (def seq-val (seq val-list))
   (loop [i 0 my-vec []]
     (if (< i (count seq-val))
       (let [my-val (nth seq-val i)
             data-val (val my-val)
             data-type (get-datatype data-val)]
         (def new-objval (obj-val. data-type data-val))
         (recur (inc i) (conj my-vec new-objval))
         )
       my-vec
       )
     )
    )
  ([entity-definition val-list]
   (def seq-val (seq val-list))
   (loop [i 0 my-vec []]
     (if (< i (count seq-val))
       (let [my-val (nth seq-val i)
             key-val (key my-val)
             data-val (val my-val)
             data-type (-> (enumobj/GetEnumByKey entity-definition :name (fx_obj/to-id key-val)) :data-type)]
         (def new-objval (obj-val. data-type data-val))
         (recur (inc i) (conj my-vec new-objval))
         )
       my-vec
       )
     )
    )
  )

(defn- get-record-values
  ([entity]
   (def val-list (get-record-values-list entity))
    (get-record-values-objdata-list val-list)
    )
  ([entity-definition entity]
   (def val-list (get-record-values-list entity-definition entity))
    (get-record-values-objdata-list entity-definition val-list)
    )
  )

(defn- get-record-keys-cl [record]
  (def key-list (get-record-keys-list record))
  (loop [i 0 my-string ""]
    (if (< i (count key-list))
      (do
        (recur (inc i) (str my-string (cond (= i 0) "" :else ", ") (nth key-list i) ))
        )
      (do
        (str my-string)
        )
      )
    )
  )

(defn- insert-cl-mapper [record-keys]
  (apply str (butlast (apply str (repeatedly (count record-keys) #(str "?,")))))
  )

(defn- insert-cl
  ([entity]
   (let [record-keys (get-record-keys-list entity)]
     (insert-cl-mapper record-keys)
     )
    )
  )

(defn- update-cl-mapper [record-keys]
  (apply str (butlast (apply str (map #(str % "= ?,") record-keys))))
  )

(defn- update-cl
  ([entity]
   (let [record-keys (get-record-keys-list entity)]
     (update-cl-mapper record-keys)
     ))
  ([entity-definition entity]
   (let [record-keys (get-record-keys-list entity-definition entity)]
     (update-cl-mapper record-keys)
     ))
  )

(defn- where-cl [clauses]
  (loop [i 0 sql-str ""]
    (if (< i (count clauses))
      (do
        (def clause (nth clauses i))
        (def sql
          (str
            (cond (nil? (:field clause)) "" :else (str " " (:field clause) " " (:filter clause) " " "?"))
            (cond (nil? (:condition clause)) "" :else (str " " (:condition clause) " "))
            (cond (nil? (:Where-Cl clause)) "" :else (str " (" (where-cl (:Where-Cl clause)) ") "))))
        (recur (inc i) (str sql-str sql))
        )
      sql-str
      )
    )
  )

(defn- create-prep-stmnt
  ([prep-stmt x clauses]
    (loop [i 0 set-cnt x]
      (if (< i (count clauses))
        (do
          (def clause (nth clauses i))
          (set-data prep-stmt set-cnt (:value clause))
          (if-not (nil? (:Where-Cl clause))
            (create-prep-stmnt prep-stmt (+ set-cnt 1) (:Where-Cl clause))
            )
          (recur (inc i) (inc set-cnt))
          )
        prep-stmt
        )
      )
    )
  )

(defn- prep-statement
  ([prep-stmt clauses]
   (create-prep-stmnt prep-stmt 1 clauses))
  ([prep-stmt entity-values clauses]
   (let [prep-entity-vals (create-prep-stmnt prep-stmt 1 entity-values)]
     (create-prep-stmnt prep-entity-vals (inc (count entity-values)) clauses))
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
  [db-spec entity where-clauses]
  (def conn (jdbc/get-connection db-spec))
  (def sql (str "select " (get-record-keys-cl entity) " from " (get-record-name entity) (cond (nil? where-clauses) "" :else (str " where " (where-cl where-clauses)))))
  (def prep-stmt (.prepareStatement conn sql))
  (def sql-ps (prep-statement prep-stmt where-clauses))
  (def results (.executeQuery sql-ps))

  (def vec-data
    (loop [i 0 rs-rlt (.next results) vec-buffer []]
      (if (< i (.getRow results))
        (do
          (def rslt (make-hash results))
          (recur (inc i) (def rs-rlt (.next results)) (conj vec-buffer rslt))
          )
        (conj vec-buffer rslt)))
    )
  (.close conn)
  vec-data
  )

(defn db-insert
  "Add"
  ([db-spec entity]
   (def conn (jdbc/get-connection db-spec))
   (def sql (str "insert into "  (get-record-name entity) " (" (get-record-keys-cl entity) ") " "values (" (insert-cl entity) ")"))
   (def prep-stmt (.prepareStatement conn sql))
   (def sql-ps (prep-statement prep-stmt (get-record-values entity)))
    (def results (.executeUpdate sql-ps))
   (.close conn)
    )
  ([db-spec entity-definition entity]
   (def conn (jdbc/get-connection db-spec))
   (def sql (str "insert into "  (get-record-name entity) " (" (get-record-keys-cl entity) ") " "values (" (insert-cl entity)  ")"))
   (def prep-stmt (.prepareStatement conn sql))
   (def sql-ps (prep-statement prep-stmt (get-record-values entity)))
    (def results (.executeUpdate sql-ps))
   (.close conn)
    )
  )

(defn db-update
  "update"
  ([db-spec entity where-clauses]
   (def conn (jdbc/get-connection db-spec))
   (def sql (str "update "  (get-record-name entity) " set " (update-cl entity) " " (cond (nil? where-clauses) "" :else (str " where " (where-cl where-clauses)))))
    (def prep-stmt (.prepareStatement conn sql))
    (def sql-ps (prep-statement prep-stmt (get-record-values entity) where-clauses))
    (def results (.executeUpdate sql-ps))
   (.close conn)
    )
  ([db-spec entity-definition entity where-clauses]
   (def conn (jdbc/get-connection db-spec))
   (def sql (str "update "  (get-record-name entity) " set " (update-cl entity-definition entity) " " (cond (nil? where-clauses) "" :else (str " where " (where-cl where-clauses)))))
   (def prep-stmt (.prepareStatement conn sql))
   (def sql-ps (prep-statement prep-stmt (get-record-values entity-definition entity) where-clauses))
   (def results (.executeUpdate sql-ps))
   (.close conn)
    )
  )

(defn db-delete
  "Delete"
  [db-spec entity where-clauses]
  (def conn (jdbc/get-connection db-spec))
  (def sql (str "delete " (get-record-name entity) (cond (nil? where-clauses) "" :else (str " where " (where-cl where-clauses)))))
  (def prep-stmt (.prepareStatement conn sql))
  (def sql-ps (prep-statement prep-stmt where-clauses))
  (def results (.executeUpdate sql-ps))
  (.close conn)
  )
