(ns clojureplayground.perf)


(comment
  (set! *warn-on-reflection* true))


(defn ^Float asum-sq-with-hint
  "Example chap 12 perf with hint"
  [^floats xs]
  (let [^floats dbl (amap xs i ret
                  (* (aget xs i)
                     (aget xs i)))]
    (areduce dbl i ret 0
             (+ ret (aget dbl i)))))


(defn asum-sq
  "Example chap 12 perf with no hint"
  [xs]
  (let [dbl (amap xs i ret
                  (* (aget xs i)
                     (aget xs i)))]
    (areduce dbl i ret 0
             (+ ret (aget dbl i)))))



(defn zencat1
  "Concat two vectors"
  [x y]
  (loop [src y ret x]
    (if (seq src)
      (recur (next src) (conj ret (first src)))
      ret)))


(defn zencat2
  "Concat two vectors"
  [x y]
  (loop [src y ret (transient x)]
    (if (seq src)
      (recur (next src) (conj! ret (first src)))
      (persistent! ret))))
