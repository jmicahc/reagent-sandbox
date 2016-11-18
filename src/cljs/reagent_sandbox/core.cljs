(ns reagent-sandbox.core
  (:require [reagent.core :as reagent]
            [reagent-sandbox.data :as data]
            [reagent-sandbox.graph :as graph]
            [reagent-sandbox.keymap :as km :refer [tag->fn]]
            [reagent-sandbox.init :as init :refer [init]]
            [reagent-sandbox.element :refer [base-element]]
            [cljs.pprint :refer [pprint]]
            [cljs.core.async :refer [chan put!]]))


(enable-console-print!)

(defn render
  ([{:keys [tags children] :as node}]
   (let [f (transduce (comp (map tag->fn))
                      comp
                      tags)]
     (into (f node base-element)
           (mapv render children)))))




(defn root-component [state]
  (fn [] [:div {:id "root-elem"} (render @state)]))




(defn reload []
  (reagent/render [root-component data/state]
                  (.getElementById js/document "app")))



(defn ^:export main []
  (reset! data/state (init data/db))
  (reload))


