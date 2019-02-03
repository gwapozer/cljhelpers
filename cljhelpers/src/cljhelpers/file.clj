(ns cljhelpers.file)

(defn get-app-path [] (str (-> (java.io.File. "") .getCanonicalPath)))

(defn readEdnFile
  [file-path]
  (let [data (slurp file-path)] (read-string data))
  )
