(ns api.clientes
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [environ.core :refer [env]]))

(def supabase-url (env :SUPABASE_URL))
(def supabase-key (env :SUPABASE_KEY))

(defn insert-cliente [email numero]
  (let [url (str supabase-url "/rest/v1/clientes")
        body {:email email
              :numero numero}
        resp (http/post url
                        {:headers {"apikey" supabase-key
                                   "Authorization" (str "Bearer " supabase-key)
                                   "Content-Type" "application/json"}
                         :body (json/generate-string body)
                         :as :json})]
    (:body resp)))


(defroutes clientes-post-routes
  (POST "/clientes" {body :body}
    (let [{:keys [email numero]} (json/parse-string (slurp body) true)
          result (insert-cliente email numero)]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string result)})))





(defn fetch-clientes []
  (let [url (str supabase-url "/rest/v1/clientes") ; REST endpoint for "clientes"
        resp (http/get url
                       {:headers {"apikey" supabase-key
                                  "Authorization" (str "Bearer " supabase-key)}
                        :as :json})]
      (:body resp)))



(defroutes clientes-get-routes
  (GET "/clientes" []
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/generate-string (fetch-clientes))}))



