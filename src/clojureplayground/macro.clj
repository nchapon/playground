(ns clojureplayground.macro)

(defmacro printer [s]
  (println "Compile : " s)
  (list 'println "Runtime :" s))


(defmacro printer-broken [s]
  (list 'println "Runtime :" s)
  (println "Compile time : " s))


(defmacro unless [test & exprs] ;; exprs is a list
  `(if-not ~test
     (do ~@exprs))) ;; Unquote splice reader macro : splice the content of the list into the container


(defn exhibits-oddity? [x]
  (unless (even? x)
    (println "Very Odd")
    (println "Very Odd, indeed!!!")))

(defmacro def-logged-fn
  [fn-name args body]
  `(defn ~fn-name ~args
     (let [now# (System/currentTimeMillis)] ;;auto-gensym
       (println "[" now# "] Call to " (str (var ~fn-name))))
     ~@body))
