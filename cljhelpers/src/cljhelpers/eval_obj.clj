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

(def test-var (iterate-obj ['(6 4 4) (+ 3 9 4)] nil))
