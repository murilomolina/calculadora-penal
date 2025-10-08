(defproject api "0.1.0-SNAPSHOT"
  :description "API Calculadora Penal"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring "1.9.6"]
                 [compojure "1.7.0"]
                 [clj-http "3.12.3"]   ;; para fazer requisições HTTP
                 [cheshire "5.11.0"]   ;; para parsear e gerar JSON
                 [environ "1.2.0"]    ;; para gerenciar variáveis de ambiente
                 ]
  :main ^:skip-aot api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})