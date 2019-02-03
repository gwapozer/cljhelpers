(ns cljhelpers.plugin-test
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [cljhelpers.plugin :as pl]
    )
  (:import (javas plugin))
  )

(defn load_jar []

  ;(pl/add-to-classpath "C:\\jars\\plugin.jar")
  ;(pl/add-to-classpath "C:\\jars\\plugin.jar")
  ;
  ;(require 'plugin.core)
  ;(println (eval (read-string  "(plugin.core/testprint)")))
  ;(println (eval (read-string  "(plugin.core/add 5 7)")))
  )

(defn loadjarfiles[]

  ;(try
  ;
  ;  (plugin/addtoClassPath "C:\\jars\\plugin.jar")
  ;
  ;  (require 'plugin.core)
  ;  (if (find-ns 'plugin.core)
  ;    (println "Namespace found")
  ;    (println "Namespace not found")
  ;    )
  ;
  ;  (println (eval (read-string  "(plugin.core/testprint)")))
  ;  (println (eval (read-string  "(plugin.core/add 5 7)")))
  ;
  ;  (catch Exception e
  ;    (println e)
  ;    ))
  )

;(loadjarfiles)
