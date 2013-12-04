(ns grott.entities.core)

(def ids (atom 0))

(def being-types {:humanoid "humanoid"
                  :lagomorph "lagomorph"
                  :lichen "lichen"
                  :insect "insect"})

(def vitality {:living "this being is alive and healthy"
               :ill "this being "})

(defprotocol Entity
  (tick [this world]
    "Update the world to handle the passing of a tick for this entity.")
  (get-type-of-being [this]
    "Get the type of being that this Entity is. E.g. 'humanoid' or 'lagomorph'"))

(defn get-id []
  (swap! ids inc))

(defn make-fnmap
  "Make a function map out of the given sequence of fnspecs.

  A function map is a map of functions that you'd pass to extend.  For example,
  this sequence of fnspecs:

  ((foo [a] (println a)
   (bar [a b] (+ a b)))

  Would be turned into this fnmap:

  {:foo (fn [a] (println a))
   :bar (fn [a b] (+ a b))}

  "
  [fns]
  (into {} (for [[label fntail] (map (juxt first rest) fns)]
             [(keyword label)
              `(fn ~@fntail)])))

(defn make-fnheads
  "Make a sequence of fnheads of of the given sequence of fnspecs.

  A fnhead is a sequence of (name args) like you'd pass to defprotocol.  For
  example, this sequence of fnspecs:

  ((foo [a] (println a))
   (bar [a b] (+ a b)))

  Would be turned into this sequence of fnheads:

  ((foo [a])
   (bar [a b]))

  "
  [fns]
  (map #(take 2 %) fns))

(defmacro defaspect
  "Define an aspect with the given functions and default implementations.

  For example:

  (defaspect Fooable
    (foo [this world]
      (println \"Foo!\"))
    (can-foo? [this world]
      (contains? world :foo)))

  This will define a Clojure protocol Fooable with the given functions as usual.
  It will also attack the function implementations as metadata, which is used by
  the add-aspect macro.  Aside from the metadata, Fooable is a normal Clojure
  protocol.

  "
  [label & fns]
  (let [fnmap (make-fnmap fns)
        fnheads (make-fnheads fns)]
    `(do
       (defprotocol ~label
         ~@fnheads)
       (def ~label
         (with-meta ~label {:defaults ~fnmap})))))

(defmacro add-aspect
  "Add an aspect to an entity type.

  This is similar to extend-type, with two differences:

  * It must be used on a protocol defined with defaspect.
  * It will use the aspect's default function implementation for any functions
    not given.

  This allows us to define common aspect functions (like can-move? and move for
  Mobile) once and only once, while still allowing them to be overridden to
  customize behavior.

  For example:

  (add-aspect Fooer Fooable
    (foo [this world]
      (println \"Bar!\")))

  This will extend the type Fooer to implement the Fooable protocol.  It will
  use the default implementation of can-foo? that was defined in the addaspect
  call, but overrides the implementation of foo to do something special.

  "
  [entity aspect & fns]
  (let [fnmap (make-fnmap fns)]
    `(extend ~entity ~aspect (merge (:defaults (meta ~aspect))
                                    ~fnmap))))

(defrecord BaseStats [strength
                      endurance
                      dexterity
                      agility
                      psyche
                      will
                      sight
                      hearing
                      smell
                      taste])

(defrecord DerivedStats [])

(defrecord Status [bleeding#
                   trauma#])

(defrecord Skill [name
                  description
                  experience
                  synergies])

(defn make-skill
  ([name]
     (make-skill name "" 0 '()))
  ([name synergies]
     (make-skill name "" 0 synergies))
  ([name description synergies]
     (make-skill name description 0 synergies))
  ([name description experience synergies]
     (map->Skill {:name name
                  :description description
                  :experience experience
                  :synergies synergies})))

(def skill-map {:spot (make-skill "spot"
                                  '(:psyche :sight))
                :mettle (make-skill "mettle"
                                    "This skill determines how well an entity reacts and adapts to combat."
                                    '(:will :psyche :endurance))
                :unarmed-combat (make-skill "unarmed combat"
                                            '(:strength :endurance :will))})


