(ns reagent-sandbox.init
  (:require [cljs.core.async :refer [put! chan]]
            [reagent-sandbox.keymap :as km :refer [tag->fn]]
            [reagent-sandbox.data :as data]))


(def buffer-size 15)


(defn init-paths
  ([node] (init-paths [] node))
  ([path {:keys [children] :as node}]
   (assoc node
          :path path
          :children (mapv (fn [idx child]
                            (init-paths (conj path :children idx)
                                        child))
                          (range)
                          children))))



(defn init-dom-listeners
  ([{:keys [children path] :as node}]
   (let [dom-events (:dom-events (meta node))
         event-fn (fn [event]
                    (put! (chan buffer-size (:xform (meta node)))
                          {:name (keyword (.-name event))
                           :path path
                           :source (get-in @data/state path)
                           :playload event}))]
     (apply assoc
            node
            :children (mapv init-dom-listeners children)
            :dom-events dom-events
            :event-fn (repeat event-fn)))))



(defn init-components
  ([{:keys [tags children] :as node}]
   (let [f (transduce (map (fn [tag]
                             (let [f (tag->fn tag)]
                               ((f node) :init identity))))
                      comp
                      tags)]
     (let [x (f (assoc node :children (mapv init-components children)))]
       x))))


(def init (comp init-dom-listeners
                init-paths
                init-components))
