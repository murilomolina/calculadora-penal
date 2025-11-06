(ns api.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [api.routes :refer [app-routes]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            ))


(def app
  (-> app-routes
      (wrap-cors
        :access-control-allow-origin [#".*"]
        :access-control-allow-methods [:get :post :options]
        :access-control-allow-headers ["Content-Type"])))


(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (run-jetty app {:port port :join? false})))
