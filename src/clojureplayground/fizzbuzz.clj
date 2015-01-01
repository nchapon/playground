(ns clojureplayground.fizzbuzz)


(defn fizzbuzz
  "FizzBuzz"
  []
  (map #(cond
         (zero? (mod % 15)) "FizzBuzz"
         (zero? (mod % 5)) "Buzz"
         (zero? (mod % 3)) "Fizz"
         :else %)
       (range 1 101)))
