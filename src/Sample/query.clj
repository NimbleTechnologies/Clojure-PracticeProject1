(ns Sample.query)

(use '[datomic.api :only [q db] :as d])
(use 'Sample.database)
(use 'clojure.pprint)

(def results (q '[:find ?n :where [?n :concept_relation/concept_ncid ?b][(= ?b 248)]] (db conn)))
(println (count results))
(pprint results)
(pprint (first results))

(def id (ffirst results))
(def entity (-> conn db (d/entity id)))

;; getting the parent of a concept.
;; TODO: find all the parents recursively
(println (:concept_relation/concept_relation_ncid entity))

(defn search-concept [str concept_ncid]
    (q '[:find ?n :in $ ?concept_ncid :where [?n :concept_relation/concept_ncid ?b][(= ?b ?concept_ncid)]] (db conn) concept_ncid))