(ns enums.enumerator)

(defn GetEnumByKey [seq id val]
  (first (filter #(= (-> % id) val) seq))
  )

(defn GetEnumList [seq id]
  (filter #(-> % id) seq)
  )
