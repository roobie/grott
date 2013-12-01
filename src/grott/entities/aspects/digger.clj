(ns grott.entities.aspects.digger
  (:use [grott.entities.core :only [defaspect]]
        [grott.world.core :only [check-tile set-tile-floor]]))


(defaspect Digger
  (dig [this dest world]
    {:pre [(can-dig? this dest world)]}
    (set-tile-floor world dest))
  (can-dig? [this dest world]
    (check-tile world dest #{:wall})))
