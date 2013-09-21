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


(defmulti compiler :os) ;;method dispatch

(defmethod compiler ::unix [m] (get m :c-compiler))

(defmethod compiler ::osx [m] (get m :c-compiler))

(def clone (partial beget {}))

(def unix {:os ::unix :c-compiler "cc" :home "/home"})

(def osx (-> (clone unix)
             (put :os ::osx)
             (put :c-compiler "gcc")
             (put  :home "/Users")))

(defmulti cmd-compile (juxt :os compiler))

(defmethod cmd-compile [::osx "gcc"] [m]
  (str "/usr/bin/" (get m :c-compiler)))

(defmethod cmd-compile :default [m]
  (str "Ensure where to locate " (get m :c-compiler)))
