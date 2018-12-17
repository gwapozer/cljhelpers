(defproject cljhelpers "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ;:repl-options  {:init-ns _main.core}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot _main.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}

  )
