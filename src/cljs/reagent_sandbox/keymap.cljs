(ns reagent-sandbox.keymap
  (:require [reagent-sandbox.layout :as layout]
            [reagent-sandbox.style :as style]
            [reagent-sandbox.event :as event]))


(defn tag->fn [tag]
  (case tag
    :layout/root   layout/layout-root
    :layout/row    layout/layout-row
    :layout/column layout/layout-column
    :style/root    style/style-element
    :event/root    event/event))
