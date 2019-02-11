(ns cljhelpers.jni_test
  (:require [clojure.test :refer :all])
  )

(defn testJNI
  []
  (println "Testing TETSJNI")
  ;(def data (TestJNI/print "test"))
  ;(def testdata (TestJNI/testprint))

  (def ipdata (JniApp/main (into-array String [])))

  (println ipdata)
  ;(println testdata)
  )

(testJNI)
