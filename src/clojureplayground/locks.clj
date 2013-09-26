(ns clojureplayground.locks
  "Joy Of Clojure Chapter 11.5 when to Use locks"
  (:use [clojureplayground.refs :only [dothreads!]])
  (:refer-clojure :exclude [aget aset count seq])
  (:require [clojure.core :as clj]))



(defprotocol SafeArray
  (aset  [this i f])
  (aget  [this i])
  (count [this])
  (seq   [this]))


(defn make-dumb-array [t sz]
  (let [a (make-array t sz)]
    (reify
      SafeArray
      (count [_] (clj/count a))
      (seq   [_] (clj/seq a))
      (aget  [_ i] (clj/aget a i))
      (aset  [this i f]
        (clj/aset a i (f (aget this i)))))))

;; Make safe array with a lock
;; locking macro could be reentrant & release the lock when exceptions
(defn make-safe-array [t sz]
  (let [a (make-array t sz)]
    (reify
      SafeArray
      (count [_] (clj/count a))
      (seq   [_] (clj/seq a))
      (aget  [_ i] (locking a
                       (clj/aget a i)))
      (aset  [this i f]
        (locking a
            (clj/aset a i (f (aget this i))))))))

;; Possible to use http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReentrantLock.html


(defn pummel [a]
  "Rouer de coups"
  (dothreads! #(dotimes [i (count a)] (aset a i inc)) :threads 100))


(def D (make-dumb-array Integer/TYPE 8))

(def A (make-safe-array Integer/TYPE 8))




;; (pummel D)
;; (seq D)
;; => (86 85 84 85 27 82 87 87)

;; (pummel A)
;; (seq A)
;; => (100 100 100 100 100 100 100 100)
