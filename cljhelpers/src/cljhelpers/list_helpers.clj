(ns cljhelpers.list_helpers)

(defn LoadCmb [cmbnode list]
  (-> cmbnode (.setItems list))
  )
