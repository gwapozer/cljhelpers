(ns cljhelpers.fx-test
  (:require [clojure.java.io :as io]
            [cljhelpers.fx_obj :as fo]
            [widget.evalobj-gui :as eg]
            [cljhelpers.eval-obj :as eo]
            [entity.id-name :refer :all]
            [cljhelpers.list_helpers :refer :all]
            )
  (:import (javafx.scene SceneBuilder)
           (javafx.scene.control ButtonBuilder)
           (javafx.scene.layout VBoxBuilder)
           (javafx.stage StageBuilder)
           (javafx.fxml FXMLLoader)
           (javas fxhelpers)
           (javafx.event EventHandler ActionEvent)
           (javafx.collections FXCollections)
           (javafx.scene.control.cell MapValueFactory PropertyValueFactory))
  (:use cljhelpers.FxRun)
  )

(def stage (atom nil))

(def loc (clojure.java.io/resource "TestGUI.fxml"))
(def loader  (FXMLLoader/load loc))

(run-now
  (reset! stage
          (.. StageBuilder create
              (title "Hello JavaFX")
              (scene (.. SceneBuilder create
                         (height 480) (width 640)
                         (root loader)
                         build))
              build)
          )
  )

(defn settext []
  ;(run-later (-> (fo/fxnode :txtinfo) (.setText "add data data")))
  (-> (fo/fxnode :txtinfo) (.setText "add data data"))
  (-> (fo/fxnode :ddlid-name) (.setValue "data2"))
  )

(defn comboselect []

  (def select-val (-> (fo/fxnode :ddlid-name) (.getValue)))
  (-> (fo/fxnode :txtinfo) (.setText select-val))
  ;(run-later (-> (fo/fxnode :txtinfo) (.setText select-val)))
  )

(defn load-gui []

  (fo/set-atom-stage @stage)
  (run-now (.show @stage))
  (run-now (-> (fo/fxnode :txtinfo) (.setText "some data")))

  (def idname-data (FXCollections/observableArrayList '((id-name 1 "data1") (id-name 2 "data2"))))
  (def combo-data (FXCollections/observableArrayList '("data1" "data2" "data3")))
  (def combo-mapdata (FXCollections/observableMap {:id 1 :name "test"}))

  (run-now (LoadCmb (fo/fxnode :ddlid-name) combo-data))
  (run-now (-> (fo/fxnode :ddlid-name) (.setValue "data1")))

  (run-now (-> (fo/fxnode :tblvinfo) (.setCellValueFactory (PropertyValueFactory. (String.)))))

  ;(run-now (-> (fo/fxnode :tblvinfo) (.setCellValueFactory (MapValueFactory. (str)))))
  (run-now (-> (fo/fxnode :tblvinfo) (.setItems combo-data)))

  (run-now (-> (fo/fxnode :btnrun) (.setOnAction (proxy [EventHandler] [] (handle [^ActionEvent event] (settext))))))
  (run-now (-> (fo/fxnode :ddlid-name) (.setOnAction (proxy [EventHandler] [] (handle [^ActionEvent event] (comboselect))))))

  ;(run-now (.show @stage))
  ;(reset! (fo/atom-stage) 5)

  ;(def getdata (run-now (-> (fo/fxnode :ddlid-name) (.getItems) (.add "sdfsf"))))
  ;(def msg (eo/iterate-obj [ getdata ] ""))
  ;(run-now (eg/LoadEvalMessage msg))
  )

(load-gui)
