(ns cljhelpers.list_helpers
  (:import (javafx.scene.control.cell MapValueFactory)))

(defn LoadCmb [cmbnode datas]
  (-> cmbnode (.setItems datas))
  )

(defn LoadTblvw [tblvwnode datas key-names]
  (-> tblvwnode (.setItems datas))

  ;TODO simplify by doing a map
  (def tablecols (-> tblvwnode (.getColumns)))
  (loop [i 0]
    (if (< i (count tablecols))
      (do
        (def tblcol (nth tablecols i))
        (-> tblcol (.setCellValueFactory (MapValueFactory. (some #{(.getId tblcol)} key-names))))
        (recur (inc i))
        )))
  ;(map #(-> % (.setCellValueFactory (MapValueFactory. (some #{(.getId %)} key-names)))) tablecols)
  ;END
  )