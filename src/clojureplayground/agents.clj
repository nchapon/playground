(ns clojureplayground.agents
  (:use [clojureplayground.refs :only [dothreads!]]))


(def log-agent (agent 0))


(defn do-log
  [msg-id msg]
  (println msg-id ":" msg)
  (inc msg-id))

;; Send off do in fact :
;; send-off (apply action-fn state-of-agent args)

(defn do-step [channel msg]
  (Thread/sleep 1)
  (send-off log-agent do-log (str channel msg)))

(defn three-step
  [channel]
  (do-step  channel " ready to begin (step 0) ")
  (do-step  channel " warming up (step 1) ")
  (do-step  channel " really getting now (step 2) ")
  (do-step  channel " done (step 3) "))

;; No output in nrepl buffer only in *nrepl-server*
;; Be sure nrepl-server is not hidden to do this :
;; (setq nrepl-hide-special-buffers nil)
(defn all-together-now []
  (dothreads! #(three-step "alpha"))
  (dothreads! #(three-step "beta"))
  (dothreads! #(three-step "omega")))


(defn handle-log-error [the-agent the-err]
  (println "An action sent to the  log-agent threw " the-err))
