(ns cljhelpers.fx_obj
  (:import (javafx.scene SceneBuilder)
           (javafx.scene.control ButtonBuilder)
           (javafx.scene.layout VBoxBuilder)
           (javafx.stage StageBuilder)
           (javafx.fxml FXMLLoader)
           (javas fxhelpers)
           )
  (:require  [widget.evalobj-gui :as eg]
             [cljhelpers.eval-obj :as eo])
  )

(def atom-stage (atom nil))

(defn set-atom-stage [x]
  (reset! atom-stage x)
  )

(defn to-id [kw]
  (str(read-string (reduce str (rest (prn-str kw)))) )
  )
(defn fxnode [kw]
  (let [root (.getRoot (.getScene @atom-stage))
        nodes (fxhelpers/getAllNodes root)]
    (def node (filter #(= (.getId %) (to-id kw)) nodes))
    (if (empty? node) nil (first node)))
  )
