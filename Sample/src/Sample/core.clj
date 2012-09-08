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

(with-open [in-file (io/reader "data/concept.csv")]
  (doall
    (let [records (csv/read-csv in-file)]
      (for [onerecord records]
        (d/transact conn 
          [{:concept/ncid (nth onerecord 0),
            :concept/cid (nth onerecord 1),
            :concept/status_ncid (nth onerecord 2),
            :concept/superceded_by_ncid (nth onerecord 3),
            :concept/enterprise_ncid (nth onerecord 4),
            :concept/concept_definition (nth onerecord 5),
            :concept/comments (nth onerecord 6),
            :concept/schema_ncid (nth onerecord 7),
            :db/id #db/id[:db.part/db -1000003]}])))))

(println "done loading..");