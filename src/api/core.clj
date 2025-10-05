(ns api.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [api.routes :refer [app-routes]]))

(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (run-jetty app-routes {:port port :join? false})))
