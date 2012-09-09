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

(with-open [in-file (io/reader "data/concept.csv")]
  (d/transact conn 
         (doall (map makeconcept (csv/read-csv in-file))))
)

(println "done loading..");