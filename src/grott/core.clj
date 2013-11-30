(ns grott.core
  (:use [grott.ui.core :only [->UI]]
        [grott.ui.drawing :only [draw-game]]
        [grott.ui.input :only [get-input process-input]])
  (:require [lanterna.screen :as s]))

(defrecord Game [world uis input debug-flags])

(defn new-game []
  (map->Game {:world nil
              :uis nil
              :input nil
              :debug-flags {:show-regions false}}))

(def game-instance (ref (new-game)))

(defn run-game [screen]
  (loop [{:keys [input uis] :as game} @game-instance]
    (when (seq uis)
      (recur (if input
               (-> game
                 (dissoc :input)
                 (process-input input))
               (-> game
                 ;(update-in [:world] tick-all)
                 (draw-game screen)
                 ;(clear-messages)
                 (get-input screen)))))))

(defn main
  ([] (main :swing))
  ([screen-type]
     (letfn [(go []
               (let [screen (s/get-screen screen-type)]
                 (s/in-screen screen
                              (run-game screen))))])))

(defn -main [& args]
  (let [args (set args)
        screen-type (cond
                     (args ":swing") :swing
                     (args ":text")  :text
                     :else           :auto)]
    (main screen-type)))
