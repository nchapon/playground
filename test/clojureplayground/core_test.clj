(ns clojureplayground.core-test
    (:use midje.sweet
        clojure.test
        clojureplayground.core))

(fact "Permutations of string abc"
      (permutations "abc") => (contains ["abc" "acb" "bac"]))


(facts "IntegerToRoman"
      (int-to-roman 1) => "I"
      (int-to-roman 2) => "II"
      (int-to-roman 3) => "III"
      (int-to-roman 4) => "IV"
      (int-to-roman 5) => "V"
      (int-to-roman 6) => "VI"
      (int-to-roman 7) => "VII"
      (int-to-roman 8) => "VIII"
      (int-to-roman 9) => "IX"
      (int-to-roman 10) => "X"
      (int-to-roman 11) => "XI"
      (int-to-roman 14) => "XIV"
      (int-to-roman 22) => "XXII"
      (int-to-roman 49) => "XLIX"
      (int-to-roman 50) => "L"
      (int-to-roman 51) => "LI"
      (int-to-roman 99) => "XCIX"
      (int-to-roman 100) => "C"
      (int-to-roman 400) => "CD"
      (int-to-roman 500) => "D"
      (int-to-roman 900) => "CM"
      (int-to-roman 1000) => "M"
      (int-to-roman 1998) => "MCMXCVIII"
      (int-to-roman 2012) => "MMXII"
)
