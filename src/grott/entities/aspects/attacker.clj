(ns grott.entities.aspects.attacker
  (:use [grott.entities.aspects.receiver :only [send-message]]
        [grott.entities.aspects.destructible :only [Destructible take-damage
                                                    defense-value]]
        [grott.entities.aspects.learner]
        [grott.entities.core :only [defaspect]]))


(declare get-damage)

(defaspect Attacker
  (attack [this target world]
    {:pre [(satisfies? Destructible target)]}
    (let [damage (get-damage this target world)]
      (->> world
        (take-damage target damage)
        (send-message this "You strike the %s for %d damage!"
                      [(:name target) damage])
        (send-message target "The %s strikes you for %d damage!"
                      [(:name this) damage]))))
  (attack-value [this world]
    (get this :attack 1)))


(defn get-damage [attacker target world]
  (let [attack (attack-value attacker world)
        defense (defense-value target world)
        max-damage (max 0 (- attack defense))
        damage (inc (rand-int max-damage))]
    damage))
