(ns api.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [api.clientes :refer [clientes-post-routes clientes-get-routes]]
            [api.calculo :refer [calculo-routes]]))

(defroutes app-routes
  ;; rota raiz
  (GET "/" [] "api da calculadora penal OK!")


  ;; rotas:
  calculo-routes

  clientes-post-routes
  clientes-get-routes

  ;; rota de fallback
  (route/not-found "Rota não encontrada"))
