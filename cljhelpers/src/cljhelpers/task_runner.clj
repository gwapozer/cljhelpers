(ns cljhelpers.task-runner)

(defn run-task
  [task-list]
  (if (> (count (rest task-list)) 0)
    (do
      (eval (:instruction (first task-list)))
      (recur (rest task-list) )
      )
    )
  )