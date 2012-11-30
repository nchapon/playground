(ns clojureplayground.core
  (:require [clojure.math.combinatorics :as combo]))

(defn permutations
  [items] (map  #(apply str %) (combo/permutations items)))


;; Kata integer to romans
(def roman-reductions '((1000 "M") (900 "CM") (500 "D") (400 "CD") (100 "C")
                        (90 "XC") (50 "L") (40 "XL") (10 "X")
                        (9 "IX") (5 "V") (4 "IV") (1 "I")))

(defn roman-reduce [number reductions]
  "returns a list of counted glyphs"
  (if (empty? reductions) ()
      (let [[[value glyph]] reductions
            count (int (/ number value))]
        (cons (list count glyph) (roman-reduce (- number (* count value)) (rest reductions))))))



(defn int-to-roman [number]
 (apply str (flatten (map (fn [[count glyph]] (take count (repeat glyph))) (roman-reduce number roman-reductions)))))
