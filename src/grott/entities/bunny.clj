(ns grott.entities.bunny
  (:use [grott.entities.core :only [Entity
                                    get-id
                                    being-types
                                    add-aspect]]
        [grott.entities.aspects.destructible :only [Destructible]]
        [grott.entities.aspects.mobile :only [Mobile move]]
        [grott.world.core :only [find-empty-neighbor]]))


(defrecord Bunny [id glyph color being-type location hp max-hp name])

(defn make-bunny [location]
  (map->Bunny {:id (get-id)
               :name "bunny"
               :glyph "v"
               :being-type (:lagomorph being-types)
               :color :yellow
               :location location
               :hp 4
               :max-hp 4}))


(extend-type Bunny Entity
  (tick [this world]
    (if-let [target (find-empty-neighbor world (:location this))]
      (move this target world)
      world)))

(add-aspect Bunny Mobile)
(add-aspect Bunny Destructible)
