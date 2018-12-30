(ns widget.evalobj-gui
  (:import (java.awt Frame Panel)
           (javax.swing JPanel JTextArea JFrame)))

(defn LoadEvalMessage [msg]

  (def frame
    (doto
      (Frame. "Evaluator")
      (.setSize 500 500)
      ;(.setDefaultCloseOperation JFrame/DISPOSE_ON_CLOSE)
      )
    )

  (def txtarea
    (doto
      (JTextArea. 25 40)
      (.setLineWrap true)
      (.setText msg)))

  (def panel
    (doto
      (JPanel.)
      (.add txtarea)))

  (-> frame (.add panel))
  (-> frame (.setVisible true))
  )
