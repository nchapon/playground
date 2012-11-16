(ns clojureplayground.core-test
    (:use midje.sweet
        clojure.test
        clojureplayground.core))


(fact "Check permutations"
      (permutations "abc") => (contains ["abc" "acb" "bac"]))


