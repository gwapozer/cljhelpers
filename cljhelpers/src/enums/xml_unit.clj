(ns enums.xml-unit
  (:require  [entity.IdName :refer :all])
  (:import (entity.IdName IdName))
  )

(def xml-unit #{(IdName. 1 "name")
                (IdName. 2 "attribute")
                (IdName. 3 "element")})