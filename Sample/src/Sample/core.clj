(ns Sample.core)

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[datomic.api :as d])

(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Hello, World!"))

(defn hello [who] (str "hello " who " !"))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)

(def uri "datomic:free://localhost:4334//hdd")
(d/create-database uri)
(def conn (d/connect uri))

(def schema-tx (read-string (slurp "data/schema.dtm")))
(println "schema-tx:")
(pprint schema-tx)

@(d/transact conn schema-tx)

(defn nreader [filename]
  (with-open [in-file (io/reader filename)]
    (doall
      (csv/read-csv in-file))))
