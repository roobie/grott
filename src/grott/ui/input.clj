(ns grott.ui.input
  (:use [grott.world.generation :only [random-world smooth-world]]
        [grott.entities.player :only [move-player]]
        [grott.ui.core :only [->UI]])
  (:require [lanterna.screen :as s]))


(defn reset-game [game]
  (let [fresh-world (random-world)]
    (-> game
      (assoc :world fresh-world)
      (assoc :uis [(->UI :play)]))))


(defmulti process-input
  (fn [game input]
    (:kind (last (:uis game)))))

(defmethod process-input :start [game input]
  (reset-game game))


(defmethod process-input :play [game input]
  (case input
    :enter     (assoc game :uis [(->UI :win)])
    :backspace (assoc game :uis [(->UI :lose)])
    \q         (assoc game :uis [])

    \h (update-in game [:world] move-player :w)
    \j (update-in game [:world] move-player :s)
    \k (update-in game [:world] move-player :n)
    \l (update-in game [:world] move-player :e)
    \y (update-in game [:world] move-player :nw)
    \u (update-in game [:world] move-player :ne)
    \b (update-in game [:world] move-player :sw)
    \n (update-in game [:world] move-player :se)

    \R (update-in game [:debug-flags :show-regions] not)

    game))

(defmethod process-input :win [game input]
  (if (= input :escape)
    (assoc game :uis [])
    (assoc game :uis [(->UI :start)])))

(defmethod process-input :lose [game input]
  (if (= input :escape)
    (assoc game :uis [])
    (assoc game :uis [(->UI :start)])))


(defn get-input [game screen]
  (assoc game :input (s/get-key-blocking screen)))
