(ns clojureplayground.multimethod-test
  (:refer-clojure :exclude [get])
  (:use [midje.sweet]
        [clojureplayground.multimethod]))




(fact "UDP" (get (beget {:sub 0} {:super 1}) :super) => 1)



(fact (compiler unix) => "cc")

(fact (compiler osx) => "gcc")

(fact (cmd-compile osx) => "/usr/bin/gcc")

(fact (cmd-compile unix) => "Ensure where to locate cc")
