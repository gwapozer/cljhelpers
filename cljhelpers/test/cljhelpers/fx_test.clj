(ns cljhelpers.fx-test
  (:require [clojure.java.io :as io]
            [cljhelpers.fx_obj :as fo]
            [widget.evalobj-gui :as eg]
            [cljhelpers.eval-obj :as eo]
            )
  (:import (javafx.scene SceneBuilder)
           (javafx.scene.control ButtonBuilder)
           (javafx.scene.layout VBoxBuilder)
           (javafx.stage StageBuilder)
           (javafx.fxml FXMLLoader)
           (javas fxhelpers)
           )
  (:use cljhelpers.FxRun)
  )

(def stage (atom nil))

(def loc (clojure.java.io/resource "TestGUI.fxml"))
(def loader  (FXMLLoader/load loc))

(run-now
  ;(def scene  (scene (.. SceneBuilder create (height 480) (width 640)
  ;                       (root loader) build)))
  ;(def stagebuilder (.. StageBuilder create (title "Hello JavaFX") build))
  ;(.setScene stagebuilder scene build)

  ;(def stage-data
  ;  (.. StageBuilder create
  ;      (title "Hello JavaFX")
  ;      (scene (.. SceneBuilder create
  ;                 (height 480) (width 640)
  ;                 (root loader)
  ;                 build))
  ;      build)
  ;  )

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

(defn load-gui []

  (fo/set-atom-stage @stage)
  (run-now (.show @stage))
  (run-now (-> (fo/fxnode :txtinfo) (.setText  "some data")) )
  ;(run-now (.show @stage))
  ;(reset! (fo/atom-stage) 5)

  ;(def msg (eo/iterate-obj [(fo/fxnode :txtinfo) ] ""))
  ;(run-now (eg/LoadEvalMessage msg))

  )

(load-gui)
