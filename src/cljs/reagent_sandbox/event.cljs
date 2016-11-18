(ns reagent-sandbox.event
  (:require [reagent.core :as reagent]
            [reagent-sandbox.graph :as graph :refer [rx]]
            [reagent-sandbox.element :refer [attr]]
            [reagent-sandbox.data :as data
             :refer [mutate-chan debounced-mutate]]
            [cljs.core.async :refer [put! chan]]))


(declare msg-contains?)


(defn node-xform [event-map]
  (fn [rf]
    (fn
      ([] (rf))
      ([ret] (rf ret))
      ([ret msg]
       (if-let [f (event-map (:name msg))]
         (rf ret (f msg))
         (rf ret msg))))))

;; Note: don't have good language to talk
;; about different component function types.
;; Say for now functions in a map are methods.
;; A function that takes a node is called
;; a node function. In this case, "on" takes
;; a sequences of keys and functions and returns
;; a node function.

;; The situation is still confusing. Some
;; functions are maps from the state to
;; the dom. Others are used simply to describe
;; a dom. This is confusing. Not every dom element
;; takes state and returns an updated element.
;; However, we want morphisms to be able to
;; utilize element functions easily.



(def buffer-size 100)


;; Problem!!!! Render should come after init.
;; Render is only called after init.
(defn on [kfs]
  (fn [rf]
    (fn
      ([] (rf))
      ([node] (apply vary-meta
                     node
                     update
                     :event-map
                     assoc
                     kfs))
      ([{:keys [event-map] :as node} elem]
       (rf node (update elem 1 merge event-map))))))


#_(defn on [kfs]
  {:init (fn [node]
           (vary-meta node
                      update
                      :dom-events
                      clojure.set/union
                      (set (take-nth 2 kfs))))
   
   :render (fn [node elem] (apply update elem 1 assoc kfs))})






(defn event [elem]
  (on :onClick (fn [msg] (assoc msg :name :hello))))





(defn source
  [{:keys [source/event-name
           source/new-name]}]
  (on event-name
      (fn [msg] (assoc msg :name new-name))))




;; We need a name for functions
;; that correspond to properties
;; of the node and not a function
;; of the node's attributes. Nodes
;; either have this property or
;; they do not. Maybe we should call
;; these tags. for now, we call functions
;; of the  node's state methods.



(def prev
  (on boolean
      (let [prev-node (atom [])]
        (fn [msg]
          (let [x @prev-node]
            (reset! prev-node (:node msg))
            (assoc msg :prev x))))))



(def x-pos
  (on boolean
      (fn [{:keys [payload] :as msg}]
        (if-let [x (.-mouseX payload)]
          (assoc msg :x x)
          msg))))



(def y-pos
  (on boolean
      (fn [{:keys [payload] :as msg}]
        (if-let [y (.-mouseY payload)]
            (assoc msg :y y)
            msg))))




#_(def delta-x
  (merge-with comp
              prev
              x-pos
              (msg-contains? [:x-pos :prev]
                             (fn [{:keys [x prev] :as msg}]
                               (assoc msg :dx (- x (:x prev)))))))




#_(def delta-y
  (merge-with comp
              prev
              y-pos
              (msg-contains? [:y-pos :prev]
                             (fn [{:keys [y prev] :as msg}]
                               (assoc msg :dy (- y (:y prev)))))))

