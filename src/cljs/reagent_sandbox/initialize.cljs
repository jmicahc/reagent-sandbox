(ns reagent-sandbox.initialize
  (:require [cljs.core.async :refer [put! chan]]))



#_(defn init-paths
  ([node] (init-paths [] node))
  ([path {:keys [children] :as node}]
   (assoc node
          :path path
          :children
          (mapv (fn [idx child]
                  (init-paths (conj path :children idx)
                              child))
                (range)
                children))))



#_(defn init-event-listeners
  ([{:keys [children] :as node}]
   (let [{:keys [xform dom-events path]} node
         channel (chan buffer-size xform)
         event-fn (fn [event]
                    (put! channel
                          {:name (keyword (.-name event))
                           :path path
                           :source (get-in @state path)
                           :playload event}))]
     (apply assoc
            node
            :children (mapv init-event-listeners children)
            (interleave dom-events event-fn)))))



#_(defn initialize
  ([{:keys [tags children] :as node}]
   (let [f (transduce (map (fn [tag]
                             (let [f (tag->fn tag)]
                               ((f node) :init identity))))
                      comp
                      tags)]
     (f (assoc node :children (mapv initialize children))))))
