(ns definition.person_definition
  (:require  [definition.table_struct :refer :all])
  (:import (definition.table_struct table-struct ))
  )

(def person_definition #{(table-struct. 1 "ID" "ID" "INTEGER" 10 true false)
                         (table-struct. 2 "FirstName" "FirstName" "VARCHAR" 50 false false)
                         (table-struct. 3 "LastName" "LastName" "VARCHAR" 50  false false)
                         (table-struct. 3 "Email" "Email" "VARCHAR" 150 false false)
                         })