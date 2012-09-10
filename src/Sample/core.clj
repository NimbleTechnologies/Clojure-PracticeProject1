(ns Sample.core)

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Hello, World!"))

(defn hello [who] (str "hello " who " !"))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)

(def uri "datomic:free://localhost:4334//hdd")

(d/delete-database uri)
(d/create-database uri)

(def conn (d/connect uri))

(def schema-tx (read-string (slurp "data/schema.dtm")))
(println "schema-tx:")

@(d/transact conn schema-tx)

(defn parse-int [str]
  (try (Integer/parseInt str)
    (catch NumberFormatException nfe 0)))                    

(defn makeconcept [[ncid cid status_ncid superceded_by_ncid enterprise_ncid concept_definition comments schema_ncid]]
  {:concept/ncid (parse-int ncid),
   :concept/cid cid,
   :concept/status_ncid (parse-int status_ncid),
   :concept/superceded_by_ncid (parse-int superceded_by_ncid),
   :concept/enterprise_ncid (parse-int enterprise_ncid),
   :concept/concept_definition concept_definition,
   :concept/schema_ncid (parse-int schema_ncid),
   :concept/comments comments,
   :db/id #db/id[:db.part/user -1]}
  )

(defn makeconceptrelation [[concept_relation_ncid relationship_ncid concept_ncid enterprise_ncid concept_relation_id]]
  {:concept_relation/concept_relation_ncid (parse-int concept_relation_ncid),
   :concept_relation/relationship_ncid (parse-int relationship_ncid),
   :concept_relation/concept_ncid (parse-int concept_ncid),
   :concept_relation/enterprise_ncid (parse-int enterprise_ncid),
   :concept_relation/concept_relation_id (parse-int concept_relation_id),
   :db/id #db/id[:db.part/user -2]}
  )

(defn makersform [[rsform_id ncid representation enterprise_ncid up_representation]]
  {:rsform/rsform_id (parse-int rsform_id),
   :rsform/ncid (parse-int ncid),
   :rsform/representation representation,
   :rsform/enterprise_ncid (parse-int enterprise_ncid),
   :rsform/up_representation up_representation,
   :db/id #db/id[:db.part/user -3]}
  )

(defn makersformcontext [[rsform_id context_ncid preferred_score enterprise_ncid rsform_context_id]]
  {:rsform_context/rsform_id (parse-int rsform_id),
   :rsform_context/context_ncid (parse-int context_ncid),
   :rsform_context/preferred_score (parse-int preferred_score),
   :rsform_context/enterprise_ncid (parse-int enterprise_ncid),
   :rsform_context/rsform_context_id (parse-int rsform_context_id),
   :db/id #db/id[:db.part/user -4]}
  )

(println "loading concept data..");

(with-open [in-file (io/reader "data/concept.csv")]
  (d/transact-async conn 
         (doall (map makeconcept (csv/read-csv in-file))))
)

(println "loading concept relation data..");

(with-open [in-file (io/reader "data/concept_relation.csv")]
  (d/transact-async conn
              (doall (map makeconceptrelation (csv/read-csv in-file))))
  )

(println "loading rsform data..");

(with-open [in-file (io/reader "data/rsform.csv")]
  (d/transact-async conn
              (doall (map makersform (csv/read-csv in-file))))
  )

(println "loading rsform context data..")

(with-open [in-file (io/reader "data/rsform_context.csv")]
  (d/transact-async conn
              (doall (map makersformcontext (csv/read-csv in-file))))
  )

(println "done loading..");