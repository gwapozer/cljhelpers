(ns cljhelpers.var-test
  (:require [clojure.java.io :as io]
            [cljhelpers.fx_obj :as fo]
            [widget.evalobj-gui :as eg]
            [cljhelpers.eval-obj :as eo]
            [entity.id-name :refer :all]
            [cljhelpers.list_helpers :refer :all]
            )
  (:import (javafx.scene SceneBuilder Group)
           (javafx.scene.control ButtonBuilder)
           (javafx.scene.layout VBoxBuilder)
           (javafx.stage StageBuilder)
           (javafx.fxml FXMLLoader)
           (javas fxhelpers)
           (javafx.event EventHandler ActionEvent)
           (javafx.collections FXCollections)
           (javafx.scene.control.cell MapValueFactory PropertyValueFactory)
           (javax.swing JButton JLabel JPanel JFrame)
           [javafx.application Application]
           [javafx.collections FXCollections ObservableList]
           [javafx.event ActionEvent EventHandler]
           [javafx.geometry Insets]
           [javafx.scene Group Scene]
           [javafx.scene.control Button Label TableColumn TableView TextField]
           [javafx.scene.control.cell MapValueFactory TextFieldTableCell]
           [javafx.scene.layout HBox VBox]
           [javafx.scene.text Font]
           [javafx.stage Stage]
           [javafx.util Callback StringConverter]
           )
  (:use cljhelpers.FxRun)
  )

(def ^:private key-names ["fname" "lname" "email"])

(defn make-cell-factory []
  (proxy [Callback] []
    (call [p]
      (TextFieldTableCell.
        (proxy [StringConverter] []
          (toString [t] (.toString t))
          (fromString [string] string))))))

(defn testgui []
  (let [table (TableView.)
        data (FXCollections/observableArrayList
               (map #(zipmap key-names %)
                    [["Jacob", "Smith", "jacob.smith@example.com"]
                     ["Isabella", "Johnson", "isabella.johnson@example.com"]
                     ["Ethan", "Williams", "ethan.williams@example.com"]
                     ["Emma", "Jones", "emma.jones@example.com"]
                     ["Michael", "Brown", "michael.brown@example.com"]]))
        hb (HBox.)
        scene (Scene. (Group.))
        label (Label. "Address Book")
        firstNameCol (TableColumn. "First Name")
        lastNameCol (TableColumn. "Last Name")
        emailCol (TableColumn. "email")
        table-columns [firstNameCol lastNameCol emailCol]
        cell-factory (make-cell-factory)
        addFirstName (TextField.)
        addLastName (TextField.)
        addEmail (TextField.)
        addButton (Button. "Add")
        add-fields [addFirstName addLastName addEmail]
        button-action (proxy [EventHandler] []
                        (handle [e]
                          (.add data (->> (map #(.getText %) [addFirstName addLastName addEmail])
                                          (zipmap key-names)))
                          (map #(.clear %) [addFirstName addLastName addEmail])))
        vbox (VBox.)]

    (def my-stage (.. StageBuilder create
                      (title "Hello JavaFX")
                      build))

    (doto my-stage
      (.setTitle "Table View Sample")
      (.setWidth 450)
      (.setHeight 500))
    (.setFont label (Font. "Arial" 20))
    (.setEditable table true)

    (.setItems table data)
    ; ===== (1) ====
    (doto firstNameCol
      (.setMinWidth 100)
      (.setCellValueFactory (MapValueFactory. (some #{"fname"} key-names))))
    (doto lastNameCol
      (.setMinWidth 100)
      (.setCellValueFactory (MapValueFactory. (some #{"lname"} key-names))))
    (doto emailCol
      (.setMinWidth 200)
      (.setCellValueFactory (MapValueFactory. (some #{"email"} key-names))))
    ; ===== (1) ====
    (.. table getColumns (setAll (to-array table-columns)))

    (map #(.setCellFactory % cell-factory) table-columns)
    (map #(.setPromptText %1 %2) add-fields ["First Name" "Last Name" "Email"])
    (map #(.setMaxWidth %1 (.getPrefWidth %2)) add-fields table-columns)

    (.setOnAction addButton button-action)
    (.. hb getChildren (addAll (to-array [addFirstName addLastName addEmail addButton])))

    (.setSpacing hb 3)

    (doto vbox
      (.setSpacing 5)
      (.setPadding (Insets. 10 0 0 10)))

    (.. vbox getChildren (addAll (to-array [label table hb])))

    (as-> (.getRoot scene) obj (cast Group obj) (.getChildren obj) (.addAll obj (to-array [vbox])))

    (doto my-stage
      (.setScene scene)
      (.show)))
  )

;(run-now (testgui))



