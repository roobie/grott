(ns grott.core
  (:require [lanterna.screen :as s]))


(defrecord Game [world uis input debug-flags])

(defn run-game [game screen]
  ;(loop [{:keys [input uis] :as game} game])
  )

(defn new-game []
  (map->Game {:world nil
              :uis nil
              :input nil
              :debug-flags {:show-regions false}}))

(defn main
  ([] (main :swing))
  ([screen-type]
     (letfn [(go []
               (let [screen (s/get-screen screen-type)]
                 (s/in-screen screen
                              (run-game (new-game) screen))))])))

(defn -main [& args]
  (let [args (set args)
        screen-type (cond
                     (args ":swing") :swing
                     (args ":text")  :text
                     :else           :auto)]
    (main screen-type)))
