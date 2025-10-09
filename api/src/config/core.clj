;; esse core vai é usado para definir as variaveis de ambiente!

(ns config.core
  (:require [environ.core :refer [env]]))

(def supabase-url (env :supabase-url))
(def supabase-key (env :supabase-key))
