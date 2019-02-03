(ns cljhelpers.file-test
  (:require [clojure.test :refer :all]
            [cljhelpers.eval-obj :as oe]
            [widget.evalobj-gui :as egui]
            [cljhelpers.file :refer :all]
            )
  (:import (javas fileUtil))
  )

(defn readFile []
  (let [item (clojure.java.io/resource "prod.edn")
        prod (readEdnFile item)
        ;byte-data (fileUtil/readFileToByte item)
        ]
    (egui/LoadEvalMessage (oe/iterate-obj [item (.getPath item) prod] "Reading IO File"))
    )
  )

(readFile)
