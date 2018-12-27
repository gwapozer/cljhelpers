(ns cljhelpers.eval-obj
  (:require [clojure.string :as str]))

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

(defn map-obj [mapors data]
  (if (> (count (rest mapors)) 0)
    (do
      (def mapped-data (map  (first mapors) data))
      (map-obj (rest mapors) mapped-data)
      )
    (map (first mapors) data)
    )
  )

(defn update-obj [updators data]
  (if (> (count (rest updators)) 0)
    (do
      (def updated-data
        (if (vector? data)
           (map (fn [x] (update-in x (first (first updators)) (first (rest (first updators))))) data)
           (update-in data (first (first updators)) (first (rest (first updators)))))
          )
      (update-obj (rest updators) updated-data)
      )
    (if (vector? data)
      (map (fn [x] (update-in x (first (first updators)) (first (rest (first updators))))) data)
      (update-in data (first (first updators)) (first (rest (first updators)))))
    )
  )
