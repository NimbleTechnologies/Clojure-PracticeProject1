(ns Sample.server
  (:require [noir.server :as server]))

(server/load-views-ns 'Sample.views)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8084"))]
    (server/start port {:mode mode
                        :ns 'Sample})))

