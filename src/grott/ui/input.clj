(ns grott.ui.input
  (:use
   [grott.ui.core :only [->UI]])
  (:require [lanterna.screen :as s]))

(defmulti process-input
  (fn [game input]
    (:kind (last (:uis game)))))


(defmethod process-input :play [game input]
  (case input
    :enter     (assoc game :uis [(->UI :win)])
    :backspace (assoc game :uis [(->UI :lose)])
    \q         (assoc game :uis [])

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
