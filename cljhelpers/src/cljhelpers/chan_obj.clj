(ns cljhelpers.chan_obj
  (:require  [clojure.core.async :refer [>! <! >!! <!! put! take! go chan buffer close! thread alts! alts!! timeout]])
  )

(def global-chan (chan))

(defn add-chan
  [item]
  (put! global-chan item)
  )

(defn get-chan-thread
  []
  (println "In go channel")
  (close! global-chan)
  (loop [ch (go (<! global-chan))]
    (let [data (<!! ch)]
      (if (not= data nil)
        (do
          (println data)
          (recur (go (<! global-chan)))
          )
        )
      )
    )
  )

(defn get-chan
  []
  (println "In channel")
  (close! global-chan)
  (loop [ch (<!! global-chan)]
    (if (not= ch nil)
      (do
        (println ch)
        (recur (<!! global-chan))
        )
      )
    )
  )


