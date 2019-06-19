(ns cljhelpers.dbconn-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :refer :all]
            [clojure.java.jdbc :as jdbc]
            [cljhelpers.plugin :as pl]
            [cljhelpers.dborm :refer :all]
            [entity.person :refer :all]
            [definition.person_definition :refer :all]
            )
  (:import (java.sql Statement Types)
           (cljhelpers.dborm Where-Cl Param-Cl)
           (entity.person Person)
           )
  )

(use 'clojure.java.jdbc)

(println "DB")

(defn test-mssql []

  (add-classpath "file:///C:/Program Files/Java/sqljdbc_7.0/enu/mssql-jdbc-7.0.0.jre8.jar")

  ;(def db-spec {:dbtype "mssql"
  ;              :dbname "database-name"
  ;              :user "sql-authentication-user-name"
  ;              :password "password"})

  (def db-spec {:classname "com.microsoft.jdbc.sqlserver.SQLServerDriver"
                :subprotocol "sqlserver"
                :subname "//server-name:DESKTOP-D8MNA28\\SQL2014;database=test;integratedSecurity=true"})

  (let [rows (jdbc/query db-spec
                         ["select * from sys.objects  where type = 'U'"])]
    (doseq [row rows] (println (:name row))))

  ;
  ;(def db {:classname "com.microsoft.jdbc.sqlserver.SQLServerDriver"
  ;         :subprotocol "sqlserver"
  ;         :subname "//server-name:port;database=test;integratedSecurity=true"})
  ;
  ;(with-connection db
  ;                 (with-query-results rs ["select * from sys.objects  where type = 'U'"]
  ;                                     (doseq [row rs] (println (:name row)))
  ;                                     ))
  )

(defn test-h2DB []

  (pl/add-to-classpath "h2-1.4.197.jar" )
  (pl/add-to-classpath "mysql-connector-java-5.1.7-bin.jar" )

  (let [db-protocol "tcp"            ; "file|mem|tcp"
        db-host     "localhost:9092" ; "path|host:port"
        db-name     "Sample"]

    (def db-spec-h2
      {:dbtype "h2"
       :dbname "/Users/gwapozer/h2-database/my-db"
       :user "sa"
       :password ""
       })

    (def db-spec-mysql
      {:dbtype "mysql"
       :dbname "test"
       :host "localhost"
       :port "3306"
       :user "root"
       :password "test"
       }
      )

    ;;jdbc:mysql://localhost:3306/unidata
    ;
    ;(def db-spec-mysql {:classname   "com.mysql.jdbc.Driver" ; must be in classpath
    ;                   :subprotocol "mysql"
    ;                   ;:subname (str "jdbc:h2:" db-protocol "://" db-host "/" db-name)
    ;                   :subname (str "jdbc:mysql://localhost:3306/unidata")
    ;                   ; Any additional keys are passed to the driver
    ;                   ; as driver-specific properties.
    ;                   :user     "root"
    ;                   :password "unidata"}))
    ;
    ;(def db-spec-test {:classname   "org.h2.Driver" ; must be in classpath
    ;         :subprotocol "h2"
    ;         ;:subname (str "jdbc:h2:" db-protocol "://" db-host "/" db-name)
    ;         :subname (str "/Users/gwapozer/h2-database/my-db")
    ;         ; Any additional keys are passed to the driver
    ;         ; as driver-specific properties.
    ;         :user     "sa"
    ;         :password ""}))

  ;(def sqlcmd "select * from Person")
  ;(def testconn (jdbc/get-connection db-spec))

  ;(def statement (doto testconn (.createStatement)))
  ;(def statement (.createStatement testconn))
  ;(doto statement (.executeQuery  sqlcmd))

  ;(println testconn)
  ;(println statement)

  ;(.close testconn)



  ;(def test-kw (web_keyword. nil nil nil nil nil))
    ;(def test-person (Person. 1 "Tom" "Sawyer" ""))
  ;(def sql-statement (db-get conn test-person nil [(Where-Cl. "ID" "=" 1 "and" [(Where-Cl. "ID" "=" 1 nil nil)])]))
  ;(def sql-statement (db-get db-spec test-person [(Where-Cl. "ID" "=" 1 nil nil)]))
    ;(def sql-statement (db-get db-spec-mysql test-person  nil nil))

  ;  (def sql-statement (db-exec-proc db-spec-mysql false "inter_serverGetKeyVal" [(Param-Cl. "in" "web_searchlog_id" nil)
  ;                                                                          (Param-Cl. "out" "key" (Types/INTEGER))]))
  ;(println sql-statement)

  ;(def test-person2 (Person. nil "Lisa" "Simpson" ""))

  ;(db-insert db-spec (Person. nil "Marge" "Simpsons" "ms@email.com") )
  ;(db-update db-spec person_definition (Person. nil "Homer" "Simpsons" "homes@email.com") [(Where-Cl. "ID" "=" 4 nil nil)] )
  ;(db-delete db-spec test-person [(Where-Cl. "ID" "=" 6 nil nil)])

  ;(let [rows (jdbc/query db-spec
  ;                       ["select * FROM IdName "])]
  ;  (doseq [row rows] (println (:id :name row))))

  ;
  ; specify the path to your database driver
  ;
    )
  )

(test-h2DB)
