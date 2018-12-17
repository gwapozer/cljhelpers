(ns cljhelpers.plugin
  (:import (java.io File)
           (javas plugin)
           (java.net URL URLClassLoader))
  )

(defn add-to-classpath [s]

  (def f (File. s))
  (def u (.toURI f))
  (def url (.toURL u))

  (let [urlClassLoader (-> (Thread/currentThread) (.getContextClassLoader))]
    (def urls (.getURLs urlClassLoader))
    (println (str "Total classes: " (count urls)))

    ;private static final Class[] parameters = new Class[]{URL.class};
    (def urlcl (.getClass url))
    ;(def testparms (URL. url))
    ;(println testparms)
    ;end

    (def urlClass (.getClass (URLClassLoader. urls)))
    (def parms (plugin/GetParameters)) ;todo interop
    (let [method (.getDeclaredMethod urlClass "addURL" parms)]
      (.setAccessible method true)
      (.invoke method urlClassLoader (object-array [url]))
      ))
  )
