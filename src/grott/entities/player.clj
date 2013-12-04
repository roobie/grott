(ns grott.entities.player
  (:use [grott.entities.core :only [Entity
                                    ->BaseStats
                                    map->BaseStats
                                    ->DerivedStats
                                    ->Status
                                    map->Status
                                    ->Skill
                                    skill-map
                                    being-types
                                    add-aspect]]
        [grott.entities.aspects.receiver :only [Receiver]]
        [grott.entities.aspects.mobile :only [Mobile move can-move?]]
        [grott.entities.aspects.digger :only [Digger dig can-dig?]]
        [grott.entities.aspects.attacker :only [Attacker attack]]
        [grott.entities.aspects.destructible :only [Destructible]]
        ;[grott.entities.aspects.learner :only [Learner gain-experience]]
        [grott.coords :only [destination-coords]]
        [grott.world.core :only [get-entity-at]]))


(defrecord Player [id
                   name
                   glyph
                   color
                   location
                   being-type
                   hp
                   max-hp
                   base-stats
                   derived-stats
                   status
                   skills
                   attack])

(extend-type Player Entity
  (tick [this world]
    world)
  (get-type-of-being [this]
    (:being-type this)))

(add-aspect Player Mobile)
(add-aspect Player Digger)
(add-aspect Player Attacker)
(add-aspect Player Destructible)
(add-aspect Player Receiver)
;(add-aspect Player Learner)

(defn make-player [location]
  (map->Player {:id :player
                :name "player"
                :glyph "@"
                :color :white
                :being-type (:humanoid being-types)
                :location location
                :hp 40
                :max-hp 40
                :attack 10
                :base-stats (map->BaseStats {:strength 10
                                             :endurance 10
                                             :dexterity 10
                                             :agility 10
                                             :psyche 10
                                             :will 10
                                             :sight 10
                                             :hearing 10
                                             :smell 10
                                             :taste 10})
                :status (map->Status {:bleeding# 0
                                      :trauma# 0})
                :skills '((:spot skill-map)
                          (:unarmed-combat skill-map))}))

(defn move-player [world dir]
  (let [player (get-in world [:entities :player])
        target (destination-coords (:location player) dir)
        entity-at-target (get-entity-at world target)]
    (cond
      entity-at-target (attack player entity-at-target world)
      (can-move? player target world) (move player target world)
      (can-dig? player target world) (dig player target world)
      :else world)))
