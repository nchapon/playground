(ns clojureplayground.multimethod-test
  (:refer-clojure :exclude [get])
  (:use [midje.sweet]
        [clojureplayground.multimethod]))




;.;. A journey of a thousand miles begins with a single step. --
;.;. @alanmstokes
(fact "UDP" (get (beget {:sub 0} {:super 1}) :super) => 1)
