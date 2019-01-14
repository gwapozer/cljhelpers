(ns entity.id-name)

(defstruct idname :id :name)

(defn id-name [id name]
  (struct idname id name)
  )

(defn list-add [curr new]
  (conj curr new)
  )

(defn is-valid [id-name]
  (def msg "")
  (if-not (integer? (:id id-name))
    (str msg "id must be a numeric")
    )
  (if-not (string? (:name id-name))
    (str msg "Invalid name")
    )
  )