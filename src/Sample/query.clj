(ns Sample.query)

(def results (q '[:find ?n :where [?n :concept_relation/concept_ncid ?b][(= ?b 248)]] (db conn)))
(println (count results))
(pprint results)
(pprint (first results))

(def id (ffirst results))
(def entity (-> conn db (d/entity id)))

;; getting the parent of a concept.
;; TODO: find all the parents recursively
(println (:concept_relation/concept_relation_ncid entity))
