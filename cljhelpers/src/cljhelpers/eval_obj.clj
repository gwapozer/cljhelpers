(ns cljhelpers.eval-obj)

(defn get-set [s val]
  (if (> (count (rest s)) 0)
    (do
      (def output (concat val (first s)))
      (get-set (rest s) output)
      )
    (concat val (first s))
    )
  )

(defn iterate-obj [obj-list msg]
  (if (> (count (rest obj-list)) 0)
    (do
      (def output (str msg (prn-str (first obj-list))))
      (iterate-obj (rest obj-list) output)
      )
    (str msg (prn-str (first obj-list)))
    )
  )
