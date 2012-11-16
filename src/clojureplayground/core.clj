(ns clojureplayground.core
  (:require [clojure.math.combinatorics :as combo]))



(defn permutations
  [items] (map  #(apply str %) (combo/permutations items)))










