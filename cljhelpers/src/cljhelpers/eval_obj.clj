(ns cljhelpers.eval-obj)

(defn iterate-obj [obj-list msg]
  (if (> (count (rest obj-list)) 0)
    (do
      (def output (str msg (prn-str (first obj-list))))
      (iterate-obj (rest obj-list) output)
      )
    (str msg (prn-str (first obj-list)))
    )
  )

(defn filter-obj [filters data]
  (if (> (count (rest filters)) 0)
    (do
      (def filtered-data (filter (every-pred (first filters)) data))
      (filter-obj (rest filters) filtered-data)
      )
    (filter (every-pred (first filters)) data)
    )
  )

(defn sort-obj [comparators data]
  (if (> (count (rest comparators)) 0)
    (do
      (def sorted-data (sort (first comparators) data))
      (sort-obj (rest comparators) sorted-data)
      )
    (sort (first comparators) data)
    )
  )
