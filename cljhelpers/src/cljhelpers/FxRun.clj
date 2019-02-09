(ns cljhelpers.FxRun
  (:import (javafx.event EventHandler ActionEvent)
           (javafx.application Platform)))
(defonce force-toolkit-init (javafx.embed.swing.JFXPanel.))

(defn run-later*
  [f]
  (javafx.application.Platform/runLater f))

(defmacro run-later
  [& body]
  `(run-later* (fn [] ~@body)))

(defn run-now*
  [f]
  (let [result (promise)]
    (run-later
      (deliver result (try (f) (catch Throwable e e))))
    @result))

(defmacro run-now
  [& body]
  `(run-now* (fn [] ~@body)))

(defn event-handler*
  [f]
  (reify javafx.event.EventHandler
    (handle [this e] (f e))))

(defmacro event-handler [arg & body]
  `(event-handler* (fn ~arg ~@body)))

(defn fx-exit
  "Exit FX platform during lein uberjar @main"
  []
  (when *files-compile*
    (future
      (println "Exiting JavaFX Thread")
      (Thread/sleep 2000)
      (Platform/setImplicitExit true)
      (Platform/exit)
      )
    )
  )
