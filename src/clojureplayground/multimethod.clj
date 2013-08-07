(ns clojureplayground.multimethod
  (:refer-clojure  :exclude [get]))



(defn beget [o p]
  (assoc o ::prototype p)) ;; prototype keyword in namespace

(def put assoc)


(defn get [m k]
  (when m
    (if-let [[_ v] (find m k)]
      v
      (recur (::prototype m) k))))
