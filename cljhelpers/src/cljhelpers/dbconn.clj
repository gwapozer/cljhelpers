(ns cljhelpers.dbconn
  (require '[clojure.java.jdbc :as jdbc])
  )

(def db-spec {:dbtype "mssql"
              :dbname "database-name"
              :user "sql-authentication-user-name"
              :password "password"})

(let [rows (jdbc/query db-spec
                       ["select * from sys.objects  where type = 'U'"])]
  (doseq [row rows] (println (:name row))))

(use 'clojure.contrib.sql)
(def db {:classname "com.microsoft.sqlserver.jdbc.SQLServerDriver"
         :subprotocol "sqlserver"
         :subname "//server-name:port;database=database-name;user=sql-authentication-user-name;password=password"
         })

;Add Classpath to your C:\Program Files\Java\JDBC\sqljdbc_3.0\enu\sqljdbc4.jar
;Below code demos how to execute a simple sql select query and print it to console
;This query will print all the user tables in your MS SQL Server Database
(with-connection db
                 (with-query-results rs ["select * from sys.objects  where type = 'U'"]
                                     (doseq [row rs] (println (:name row)))
                                     ))