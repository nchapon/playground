(ns clojureplayground.string-broken
  (:use [clojure.string :only [upper-case]]
        [clojure.pprint :only [pprint]]))

(def strings ["&#x1f638;&#x1f63e;" "noël" "abc" "baffle"])

(defn broken-string?
  "broken string ?"
  [s]
  {:string s
   :length (count s)
   :reverse (apply str (reverse s))
   :tail (apply str (rest s))
   :3-chars (subs s 0 3)
   :uppercase (upper-case s)})

(defn broken-strings
  "doc-string"
  []
  (->
   (for [s strings] (broken-string? s))
   (pprint)))
