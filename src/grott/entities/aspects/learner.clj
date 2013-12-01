(ns grott.entities.aspects.learner
  (:use [grott.entities.core :only [defaspect]]
        [grott.entities.aspects.receiver :only [send-message]]))

(defaspect Learner
  (gain-experience
   [{:keys [id] :as this} exp-gained world]
   (let [smarter-this (update-in this [:common-exp] + exp-gained)]
     (send-message this "You gain %d experience" [exp-gained] world)
     (assoc-in world [:entities id] smarter-this))))
