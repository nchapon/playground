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


(defn build-contract [c]
  (let [args (first c)]
    (list
     (into '[f] c)
     (apply merge
            (for [con (rest c)]
              (cond
               (= (first con) 'require)
               (assoc {} :pre (vec (rest con)))
               (= (first con) 'ensure)
               (assoc {} :post (vec (rest con)))
               :else (throw (Exception. (str "Unkown tag :" (first con)))))))
     (list* 'f args))))

(defn collect-bodies [forms]
  (for [form (partition 3 forms)]
    (build-contract form)))

(defmacro defcontract [& forms]
  (let [name (if (symbol? (first forms))
               (first forms)
               nil)
        body (if name
               (rest forms)
               forms)]
    (list* 'fn name body)))



(def doubler-contract ;; #1_contract_comp: Define a contract
  (defcontract doubler
    [x]
    (require
     (number? x)
     (pos? x))
    (ensure
     (= (* 2 x) %))))

(def times2 (partial doubler-contract #(* 2 %))) ;; #2_contract_comp: Test correct fn
