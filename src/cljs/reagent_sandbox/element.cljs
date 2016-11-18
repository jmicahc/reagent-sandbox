(ns reagent-sandbox.element)


(def base-element (with-meta [:div {}]
                    {:dom-events #{}
                     :xform (fn [rf]
                              (fn ([] (rf))
                                ([ret] (rf ret))
                                ([ret msg]
                                 (rf ret msg))))}))



#_(defn add-key [elem]
  (assoc-in elem [1 :key] (:ident (meta elem))))


#_(defn element [& fns]
  {:render (fn [elem]
             (let [child ((apply comp add-key fns) base-element)]
               (conj elem child)))})



(defn attr [k v & kvs]
  (fn [rf]
    (fn
      ([] (rf))
      ([node] (rf node))
      ([node elem]
       (rf node (apply update elem 1 assoc k v kvs))))))

#_(defn attr
  {:render (fn [elem]
             (apply update elem 1 assoc k v kvs))})



(defn style [k v & kvs]
  (fn [rf]
    (fn
      ([] (rf))
      ([node] (rf node))
      ([node elem]
       (rf node (apply update-in elem [1 :style] assoc k v kvs))))))



(defn tag [name]
  (fn [rf]
    (fn
      ([] (rf))
      ([node] (rf node))
      ([node elem]
       (rf node (assoc elem 0 name))))))




;; Resizing is a function associated with an
;; element. The function is defined on the
;; element's descendants. The function is called
;; when a particular child source event is
;; dragged. Thus, the child must send a message
;; to the parent that applies a transform based
;; on the parent's value and position of the
;; source element.

;; The message relay must be setup somehow.
;; On initialization, the parent finds all
;; descendent source events according to some
;; marker and declares a communication relation.
;; This stateful connection is established at
;; the root of the innitialization process,
;; after the communication graph is declared.

;; Given a tree of layout elements and
;; event source elements, ...
