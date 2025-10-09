(ns api.clientes
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [config.core :as config] ;; acessar as variaveis de ambiente definidas em config.core
            ))


(defn fetch-clientes []
  (let [url (str config/supabase-url "/rest/v1/calc_penal_clientes") 
        resp (http/get url
                       {:headers {"apikey" config/supabase-key
                                  "Authorization" (str "Bearer " config/supabase-key)
                                  "Accept" "application/json"}
                        :as :json})]
    (:body resp)))


(println "Supabase URL:" config/supabase-url)
(println "Supabase Key:" config/supabase-key)

(defroutes clientes-get-routes
  (GET "/clientes" []
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/generate-string (fetch-clientes))}))



;;;;;;;;;;;;;;;;;;;;; POST clientes ;;;;;;;;;;;;;;;;;;;;;;


(defn insert-cliente [email numero processo]
  (let [url (str config/supabase-url "/rest/v1/calc_penal_clientes")
        body {:email email
              :numero numero
              :processo processo}
        resp (http/post url
                        {:headers {"apikey" config/supabase-key
                                   "Authorization" (str "Bearer " config/supabase-key)
                                   "Content-Type" "application/json"
                                   "Prefer" "return=representation"}  ;; para retornar o registro inserido
                         :body (json/generate-string body)
                         :as :json})]
    (:body resp)))


(defroutes clientes-post-routes
  (POST "/clientes" {body :body}
    (let [{:keys [email numero processo]} (json/parse-string (slurp body) true)
          result (insert-cliente email numero processo)]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string result)})))