(ns grott.entities.aspects.mobile
  (:use [grott.entities.core :only [defaspect]]
        [grott.world.core :only [is-empty?]]))


(defaspect Mobile
  (move [this dest world]
    {:pre [(can-move? this dest world)]}
    (assoc-in world [:entities (:id this) :location] dest))
  (can-move? [this dest world]
    (is-empty? world dest)))

