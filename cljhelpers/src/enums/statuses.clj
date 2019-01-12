(ns enums.statuses
  (:require [entity.id-name :refer :all])
  )

(def statuses #{(id-name 1 "start")
                (id-name 2 "stop")
                (id-name 3 "neutral")})