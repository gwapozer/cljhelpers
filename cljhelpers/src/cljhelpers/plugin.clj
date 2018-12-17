(ns cljhelpers.plugin
  (:import (java.io File)
           (java.net URL URLClassLoader)
           (java.lang.reflect Method)))

(defn add-to-classpath [s]

  (println "Start loading class ...")

  (def f (File. s))
  (def u (.toURI f))
  (def url (.toURL u))

  (println url)

  (let [urlClassLoader (-> (Thread/currentThread) (.getContextClassLoader))]
    (def urls (.getURLs urlClassLoader))
    ;(def class (URLClassLoader/class))
    (println (count urls))
  )

  (println "End loading class ...")

  )
