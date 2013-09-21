(ns clojureplayground.mutation
  (:use [clojure.pprint :as pp])
  (:import [java.util.concurrent Executors]))

;; JOY Of Clojure chapter 11 (mutation) exercices
;; Usage of refs
(def *pool* (Executors/newFixedThreadPool
             (+ 2 (.availableProcessors (Runtime/getRuntime)))))

(defn dothreads! [f & {thread-count :threads
                       exec-count :times
                       :or {thread-count 1 exec-count 1}}]
  (dotimes [t thread-count]
    (.submit *pool* #(dotimes [_ exec-count] (f))))) ;; possible because in clojure each function is a class that "implements" Callable

;; Used to start n threads whith each one launch n times function
(defn go [move-fn threads times]
  (dothreads! move-fn :threads threads :times times))



;; Reuse neighbors function from pathfinder
(defn neighbors
  ([size xy] (neighbors [[-1 0] [1 0] [0 -1] [0 1]] size xy))
  ([deltas size xy]
     (filter (fn [new-xy]
               (every? #(< -1 % size) new-xy))
             (map #(mapv + xy %) deltas))))

;; Initialize the little chess board
(def initial-board
  [[:- :k :-]
   [:- :- :-]
   [:- :K :-]])

;; map function to each element of the board
(defn board-map [f bd]
  (vec (map #(vec (for [s %] (f s))) bd))) ;; use vec to not have seq



(defn reset!
  "Reset the board state"
  []
  (def board (board-map ref initial-board)) ;; all board items are mapped to ref function
  (def to-move (ref [[:K [2 1]] [:k [0 1]]])) ;; First the piece that will move, second the enemy
  (def num-moves (ref 0))) ;; num-moves and to-move are synchronized, to get the current value @num-moves or @to-move

;; All possible moves for king
(def king-moves (partial neighbors
                         [[-1 -1] [-1 0] [-1 1] [1 0] [1 -1] [0 -1] [0 1] [1 1]] 3))

;; A move to a square is legual only if the enemy isn't located here.
(defn good-move? [to enemy-sq]
  (when (not= to enemy-sq) to))

;; Choose the first good move place int the possible neighbors
(defn choose-move
  [[[mover mpos] [_ enemy-pos]]]
  [mover (some #(good-move? % enemy-pos)
               (shuffle (king-moves mpos)))])


(defn place [from to] to) ;; used to change a piece on the board

(defn move-piece
  [[piece dest] [[_ src] _]]
  (alter (get-in board dest) place piece) ;; Place moving piece (alter [ref fun & args])
  (alter (get-in board src) place :-) ;; Empty from square
  (alter num-moves inc)) ;; Increment num noves


(defn update-to-move [move]
  (alter to-move #(vector (second %) move))) ;; Swap if K will k to move

(defn make-move
  []
  (dosync
   (let [move (choose-move @to-move)]
     (move-piece move @to-move)
     (update-to-move move))))

;; To visualize the board
(defn board-state
  []
  (do
    (pp/pprint (board-map deref board))
    (pp/pprint @num-moves)))
