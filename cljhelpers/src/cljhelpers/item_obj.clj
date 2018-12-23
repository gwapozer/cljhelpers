(ns cljhelpers.item_obj)

(defn atomizer []
  (atom nil)
  )

(defn set-atom [atom-obj x]
  (reset! atom-obj x)
  )

(defn add-item [atom-obj item]
  (reset! atom-obj (conj @atom-obj item))
  )
