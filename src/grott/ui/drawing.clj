(ns grott.ui.drawing)

;Definitions -----------------------------------------------------------------
(defmulti draw-ui
  (fn [ui game screen]
    (:kind ui)))

(defn draw-game [game screen]
  game)
