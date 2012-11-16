(ns clojureplayground.core-test
    (:use midje.sweet
        clojure.test
        clojureplayground.core))

(fact "Permutations of string abc"
      (permutations "abc") => (contains ["abc" "acb" "bac"]))


