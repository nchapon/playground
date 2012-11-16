(ns clojureplayground.queries
  (:require clojureplayground.data)
  (:import [clojureplayground.data Employee]))

(println
  "Employees named Albert:"
  (filter #(= "Albert" (.name %))
    [(Employee. "Albert" "Smith")
     (Employee. "John" "Maynard")
     (Employee. "Albert" "Cheng")]))