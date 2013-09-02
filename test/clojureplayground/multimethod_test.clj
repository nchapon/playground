(ns clojureplayground.multimethod-test
  (:refer-clojure :exclude [get])
  (:use [midje.sweet]
        [clojureplayground.multimethod]))




(fact "UDP" (get (beget {:sub 0} {:super 1}) :super) => 1)



;.;. The journey is the reward. -- traditional
(fact (compiler unix) => "cc")

(fact (compiler osx) => "gcc")
