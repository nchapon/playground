(ns clojureplayground.ddd-test
  (:use
    [clojureplayground.ddd
     :only (create-order add-item total remove-item)]
    [midje.sweet])
  (:require
    [clojureplayground.ddd])
  (:import
    [clojureplayground.ddd PurchaseOrder LineItem LineProduct]
    [org.joda.time DateTime])
  )


(def order-time (DateTime. 2012 11 13 14 20 5))

(fact "create order"
      (create-order 5 order-time 500) => (PurchaseOrder. 5 order-time :open 500 []))


(facts "Limit must be >= 100 and <= 1000"
       (create-order 5 order-time 100) => truthy
       (create-order 5 order-time 99) => (throws AssertionError)
       (create-order 5 order-time 1000) => truthy
       (create-order 5 order-time 10001) => (throws AssertionError))


(fact "Adding items already in order increments qty"
    (let [cheese (LineProduct. "cheese" 5)
          ham (LineProduct. "ham" 3)
          order-with-items (-> (create-order 1 order-time 200)
                    (add-item 2 cheese)
                    (add-item 1 ham)
                    )]
          (add-item order-with-items 2 cheese)
             => (PurchaseOrder. 1 order-time :open 200
                                [(LineItem. 4 cheese)
                                 (LineItem. 1 ham)])))

(facts "Removing items leaving zero (or negative) qty drops entire line"
    (let [cheese (LineProduct. "cheese" 5)
          ham (LineProduct. "ham" 3)
          order-with-items (-> (create-order 1 order-time 200)
                    (add-item 2 cheese)
                    (add-item 1 ham)
                    )]
          (remove-item order-with-items 2 cheese)
             => (PurchaseOrder. 1 order-time :open 200
                                [(LineItem. 1 ham)])
         
           (remove-item order-with-items 1 cheese)
             => (PurchaseOrder. 1 order-time :open 200
                                [(LineItem. 1 cheese)
                                 (LineItem. 1 ham)])  

             ))


(facts "Total protocols implementation"

       (let [line1 (LineItem. 5 (LineProduct. "cheese" 4))
             line2 (LineItem. 10 (LineProduct. "ham" 20))
             order (-> (create-order 1 order-time 200) (assoc :lines [line1 line2]))]
         (total line1) => 20
         (total line2) => 200
         (total order) => 220 
       ))


(fact "Order total must not be above order limit"
      (let [ham (LineProduct. "Ham" 100)
            cheese (LineProduct. "Cheese" 150)
            order (-> (create-order 1 order-time 200) (add-item 1 ham))]
        (add-item order 1 cheese) => (throws AssertionError)))


