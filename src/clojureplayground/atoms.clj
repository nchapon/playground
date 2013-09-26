(ns clojureplayground.atoms
  (:use [clojureplayground.refs :only [dothreads!]]))



(def *time* (atom 0))


(defn tick []
  (swap! *time* inc))

;; Used to reduce the cache size because with memoize you can't evict the cache
(defn manipulable-memoize [function]
  (let [cache (atom {})]
    (with-meta
      (fn [& args]
        (or (@cache args) ;;return value if contains in cache
            (let [ret (apply function args)] ;; else set args & value in cache
              (swap! cache assoc args ret)
              ret)))
      {:cache cache}))) ;; attach metadata



(def slowly (fn [x] (Thread/sleep 3000) x))

(def sometimes-slowly (manipulable-memoize slowly))



(comment
  (time [(slowly 9) (slowly 9)])
  (time [(sometimes-slowly 108) (sometimes-slowly 108)])
  (let [cache (:cache (meta sometimes-slowly))]
                           (swap! cache dissoc '(108)))) ;; remove cached with arg 108
