(ns Sample.database)

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)

(def uri "datomic:free://localhost:4334//hdd")
;(def uri "datomic:free://?jdbc:postgresql://localhost:5432/datomic?user=datomic&password=datomic")

(def conn (d/connect uri))