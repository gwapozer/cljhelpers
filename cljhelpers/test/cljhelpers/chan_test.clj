(ns cljhelpers.chan_test
  (:require [clojure.test :refer :all]
            [clojure.core.async :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!! timeout]]
            [cljhelpers.chan_obj :refer :all]
            ))

(defn test-chan
  []
  (println "Testing channel")
  (def c (chan))
  (>!! c "hello")
  (println (<! c))
  )

;(test-chan)

(defn testdata []

  (println "Testing channel")

  (add-chan 1)
  (add-chan 2)
  (add-chan 4)
  (add-chan 6)
  ;(close-chan)
  (get-chan)

  (println "Finish Testing channel")

  )

(testdata)
