(ns reagent-sandbox.style
  (:require [reagent.core :as reagent]
            [reagent-sandbox.element :refer [style]]))


(defn style-element [{:keys [style/backgroundColor]}]
  (style :backgroundColor
         backgroundColor))
