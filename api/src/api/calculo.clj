(ns api.calculo
  (:require [compojure.core :refer :all]
            [cheshire.core :as json]
            [clojure.string :as str]
            )
  (:import [java.time LocalDate]
           [java.time.format DateTimeFormatter]
           ))

;; penaTotal: em anos, meses e dias, EXEMPLO: "2, 3, 15"
;; inicioCumprimento: data no formato "YYYY-MM-DD"
;; tempoDetracao: em anos, meses e dias  **SE APLICAVEL**, EXEMPLO: "0, 2, 0"
;; tipoCrime ["hediondo", "comum"] , hediondo/equiparado
;; statusApenado ["primario", "reincidente"]

(defn pena_para_dias [pena-str]
    ;; "Converte uma string de pena no formato 'AA, MM, DD' para o total de dias. Exemplo: '2, 3, 15' => 837 dias"
    (let [[anos meses dias] (map #(Integer/parseInt (str/trim %))
                               (str/split pena-str #","))]
    (+ (* anos 365)
       (* meses 30)
       dias)))


(def multiplicadores
  {;; exemplos da documentação: 
   ["comum"    "primario"]      0.16
   ["comum"    "reincidente"]   0.20
   ["hediondo" "primario"]      0.40
   ["hediondo" "reincidente"]   0.50 ;; chutei esse multiplicador (não achei na documentação)
  })


(defn aplicar_condicionais [total-dias tempoDetracao tipoCrime statusApenado]
    ;; aplica as condições para redução da pena
    (let [multiplicador (get multiplicadores [tipoCrime statusApenado] 1)] ; default = 1  
        (- (* total-dias multiplicador) (pena_para_dias tempoDetracao))) 
)

(defn add-dias-data-final
  ;; "Recebe um número de dias (int ou float) e uma data inicial (DD-MM-YYYY), e retorna a data final (DD-MM-YYYY)."
  [dias data-inicial]
  (let [formatter (DateTimeFormatter/ofPattern "dd-MM-yyyy")
        data (LocalDate/parse data-inicial formatter)
        nova-data (.plusDays data dias)]
    (.format nova-data formatter)))

;; core logic
(defn calculate [penaTotal inicioCumprimento tempoDetracao tipoCrime statusApenado]
    (let [total-dias (pena_para_dias penaTotal)
        pena-condicionada (aplicar_condicionais total-dias tempoDetracao tipoCrime statusApenado)
    ]
        
    ;; (println "Total de dias de pena sem condicionais: " total-dias) ;; log
    ;; (println "Total de dias de pena com condicionais: " pena-condicionada)  ;; log

    { ;; tudo em dias
        :pena_total_dias pena-condicionada ;; Pena total em dias após aplicação das condições e com a subtração do tempo de detração
        :dias_p_semi_aberto (* pena-condicionada 0.15) ;; Data Prevista para Progressão ao Regime Semiaberto
        :dias_p_aberto (* pena-condicionada 0.30) ;; Data Prevista para Progressão ao Regime Aberto
        :dias_p_livramento_condicional (* pena-condicionada 0.45) ;; Data Prevista para o Livramento Condicional
        :inicio_Cumprimento inicioCumprimento ;; Data de Início do Cumprimento da Pena


        ;; Saídas em datas no formato DD-MM-AAAA
        ;; :data_inicio_semi_aberto ;; (dias_p_semi_aberto após o inicioCumprimento) em padrão DD-MM-AAAA
        :data_inicio_semi_aberto (add-dias-data-final (* pena-condicionada 0.15) inicioCumprimento)
        ;; :data_inicio_aberto ;; (dias_p_aberto após o inicioCumprimento) em padrão DD-  MM-AAAA
        :data_inicio_aberto (add-dias-data-final (* pena-condicionada 0.30) inicioCumprimento)
        ;; :data_inicio_livramento_condicional ;; (dias_p_livramento_condicional após o inicioCumprimento) em padrão DD-  MM-AAAA
        :data_inicio_livramento_condicional (add-dias-data-final (* pena-condicionada 0.45) inicioCumprimento)
    }
    )
)

;; define routes
(defroutes calculo-routes
  (POST "/calculo" {body :body}
    (let [{:keys [penaTotal inicioCumprimento tempoDetracao tipoCrime statusApenado]} (json/parse-string (slurp body) true)]
        (let [result (calculate penaTotal inicioCumprimento tempoDetracao tipoCrime statusApenado)]    
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string result)}))))
