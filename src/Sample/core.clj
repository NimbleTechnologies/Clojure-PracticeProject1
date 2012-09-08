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

(println "loading data..");

(defn makeconcept [[ncid cid status_ncid superceded_by_ncid enterprise_ncid concept_definition comments schema_ncid]]
  {:concept/ncid ncid,
            :concept/cid cid,
            :concept/status_ncid status_ncid,
            :concept/superceded_by_ncid superceded_by_ncid,
            :concept/enterprise_ncid enterprise_ncid,
            :concept/concept_definition concept_definition,
            :concept/comments comments,
            :concept/schema_ncid schema_ncid,
            :db/id #db/id[:db.part/db -1000003]}
  )

(with-open [in-file (io/reader "data/concept.csv")]
  (d/transact conn 
         (doall (map makeconcept (csv/read-csv in-file))))
)

(println "done loading..");