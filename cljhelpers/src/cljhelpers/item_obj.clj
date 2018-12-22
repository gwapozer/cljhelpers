(ns cljhelpers.item_obj)

(def atom-item (atom nil))

(defn set-atom [x]
  (reset! atom-item x)
  )

(defn add-item [item]
  (reset! atom-item (conj @atom-item item))
  )