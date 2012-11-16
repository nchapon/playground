(ns clojureplayground.ddd)


; Generates a java interface
(defprotocol Order

  (add-item [this qty product]
    "Add an item to the order")

  (remove-item [this qty product]
    "Remove an item to the order")

  (close-order [this]
    "Close order, it is not cooled to changed a closed order")
  )

(defprotocol Total
  (total [this] "Calculate total")
  )


(defrecord LineProduct [product piece-product])

(defrecord LineItem [qty line-product]
  Total
  (total [{:keys [qty line-product]}]
    (* qty (:piece-product line-product)))
  )

(defrecord PurchaseOrder [number date status limit lines])

(defn indexed
  "Returns a lazy sequence of [index, item] pairs, where items come
   from 's' and indexes count up from zero.

   (indexed '(a b c d)) => ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map vector (iterate inc 0) s))


(defn positions
"Returns a lazy sequence containing the positions at which pred
is true for items in coll."
  [pred coll]
  (for [[idx elt] (indexed coll) :when (pred elt)] idx))


(defn- find-line-ix [order product]
  (first (positions #(= (:line-product %) product) (:lines order))))

(extend-type PurchaseOrder
  Order
  (add-item [this qty product]
    {:post [( <= (total %) (:limit %))]}
    (if-let [ix (find-line-ix this product)]
      (update-in this [:lines ix :qty] + qty)
      (update-in this [:lines] conj (LineItem. qty product)))
  )


  (remove-item [this qty product]
    (if-let [ix (find-line-ix this product)]
      (if (> (get-in this [:lines ix :qty]) qty)
         (update-in this [:lines ix :qty] - qty)
         (assoc this :lines (remove #(= (:line-product %) product) (:lines this))))
      this)
    )


  (close-order [this])

  Total
  (total [this]
     (let [line-totals (map total (:lines this))]
       (apply + line-totals))
    )
)
    





(defn create-order [number date limit]
  {:pre [(>= limit 100) (<= limit 1000)]}
  (PurchaseOrder. number date :open limit []))


