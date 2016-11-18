(ns reagent-sandbox.layout
  (:require [reagent.core :as reagent]
            [reagent-sandbox.event :as event :refer [on]]
            [reagent-sandbox.style :as style]
            [reagent-sandbox.element :refer [attr style tag]]))




(defn layout-column [rf]
  (comp (tag :div)
        (attr :id id)))

#_(defn layout-column
  [{:keys [id layout/magnitude]}]
  (merge-with comp
              (tag :div)
              (attr :id id)
              (style :width magnitude
                     :display "flex"
                     :flexDirection "column")))





(defn layout-row
  [{:keys [id layout/magnitude] :as node}]
  (merge-with comp
              (tag :div)
              (attr :id id)
              (style :height magnitude
                     :display "flex"
                     :flexDirection "row")))



(defn layout-root
  [{:keys [id
           style/width style/height
           style/top style/left
           layout/partition]}]
  (merge-with comp
              (tag :div)
              (attr :id id)
              (style :width width
                     :height height
                     :top top
                     :left left
                     :display "flex"
                     :position "absolute"
                     :flexDirection partition)))

