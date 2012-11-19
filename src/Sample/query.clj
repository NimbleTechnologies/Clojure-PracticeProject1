(ns Sample.query)

(use '[datomic.api :only [q db] :as d])
(use 'Sample.database)
(use 'clojure.pprint)

(defn search-concept [str concept_ncid]
    (q '[:find ?n :in $ ?concept_ncid :where [?n :concept_relation/concept_ncid ?b][(= ?b ?concept_ncid)]] (db conn) concept_ncid))

; get parent of a concept. the one which has a 'Has-Child' relationship
(defn get-parent [concept_id]
           (q '[:find ?a :in $ ?concept_id :where [?n :concept_relation/concept_ncid ?concept_id][?n :concept_relation/relationship_ncid 364]
                [?n :concept_relation/concept_relation_ncid ?a]](db conn) concept_id))

; get all parents of a concept
(defn get-all-parents [n]
  (when-not (empty? n)
    (do (print n)
      (get-all-parents (first (get-parent (first n)))))));

; get the id of entity from the ncid
(defn get-concept-id [ncid]
  (first
    (q '[:find ?n :in $ ?ncid :where [?n :concept/ncid ?ncid]](db conn) ncid)));

; update the concept cid based on ncid
(defn update-concept [ncid new-cid]
  (d/transact conn [[:db/add (first (get-concept-id ncid)) :concept/cid new-cid]]));

; getting the history of a particular concept change (cid for now)
(defn all-history-ncid [db ncid]
  (q '[:find ?a :in $ ?ncid :where [?e :concept/cid ?a][?e :concept/ncid ?ncid]](d/history db) ncid));

; -------------------------------------
; utility helpers for testing
(defn get-concept-cid [ncid]
  (first
    (q '[:find ?c :in $ ?ncid :where [?n :concept/cid ?c][?n :concept/ncid ?ncid]](db conn) ncid)));
