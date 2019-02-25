(ns cljhelpers.math)

(defn pow
  [x n]
  (Math/pow x n)
  )

(defn root
  [x]
  (Math/sqrt x)
  )

(defn ln
  "natural logarithm"
  [x]
  (pow (+ 1 (/ 1 x)) x)
  )

(defn log
  "Log is Log2^n"
  [n]
  (/ (ln n) (ln 2))
  )

(defn log10
  "Log10 is Log10^n"
  [n]
  (/ (ln n) (ln 10))
  )

(defn radian
  []
  (* 3.14 180.0)
  )

(defn lenght-of-arc
  [r degree]
  (let [θ (* radian degree)]
    (* r θ)
    )
  )

(defn area-of-sector
  [r degree]
  (let [θ (* radian degree)]
    (* (/ 1 2) (* (pow r 2) θ))
    )
  )

(defn sin
  [opp hyp]
  (/ opp hyp)
  )

(defn cos
  [adj hyp]
  (/ adj hyp)
  )

(defn tan
  [adj opp]
  (/ opp adj)
  )

(defn pythagorean
  "Pythagorean formula for 90 degrees: c^2 = a^2 + b^2;"
  [adj opp hyp]
  (cond
    (> (count (filter #(nil? %) [adj opp hyp])) 1) nil
    (nil? hyp) (root (+ (pow adj 2) pow opp 2))
    (nil? opp) (root (- (pow hyp 2) (pow adj 2)))
    (nil? adj) (root (- (pow hyp 2) (pow opp 2)))
    :else nil
    )
  )

(defn o-pythagorean
  "Pythagorean formula for oblique triangle: c^2 = a^2 + b^2 - 2ab cos C; C is the angle"
  [adj opp C]
  (let [sub-formula (* (apply * [adj opp]) (Math/cos C))]
    (- (pythagorean adj opp nil) sub-formula)
    )
  )

(defn area-of-triangle
  [b h]
  (/ (* b h ) 2)
  )

(defn heron-triangle
  [a b c]
  (let [s (/ (+ a b c) 2)]
    (root (* s (- s a) (- s b) (- s c)))
    )
  )

(defn area-of-triangle-side-angle
  [a b C]
  (/ (* a b (Math/sin C)) 2)
  )
