(ns entity.person)

(def id (atom nil))
(def first-name (atom nil))
(def last-name (atom nil))

(defn person [_id _firstname _lastname]
  (swap! id _id)
  (swap! first-name _firstname)
  (swap! last-name _lastname)
  )

(defn get-id [] id [_id] (swap! id _id))
(defn get-firstname ([] first-name) ([_firstname] (swap! first-name _firstname)) )
(defn get-lastname ([] last-name) ([_lastname] (swap! last-name _lastname)) )

(defn print []
  (println id)
  (println name)
  )

(id-name 3 "test")
(print)