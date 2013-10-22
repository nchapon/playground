(ns clojureplayground.futures
  (:use [clojureplayground.refs :only [dothreads!]])
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]))



(defn task [i]
  (Thread/sleep 2000)
  (* i 2))

(defn n-tasks
  []
  (let [results (for [i (range 6)] (task i))]
    (reduce + results)))


(defn n-tasks-with-future
  "Call n tasks with futures"
  []
  (let [tasks (map #(future (task %)) (range 6))] ;; call task with 6 threads
    (reduce + (map deref tasks)))) ;; can compute result with deref function

(defn n-tasks-with-promise
  "Call n tasks with promises"
  []
  (let [tasks (take 6 (repeatedly promise))] ;; 6 placeholders where values are fullfilled in another thread
    (dotimes [i 6]
      (dothreads!
       (fn []
         (deliver (nth tasks i) (task i))))) ;; deliver the computed value to the promise
    (reduce + (map deref tasks)))) ;; deref the promises
