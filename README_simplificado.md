# 🌐 Sistema Integrado — Aplicativo, Página Web e API

## 💡 Visão Geral

Este projeto foi desenvolvido para conectar três plataformas diferentes — um **aplicativo Android**, uma **página web** e uma **API central** — em um único ecossistema.  
O objetivo é facilitar o acesso e o gerenciamento de informações em tempo real, seja por meio do celular ou do navegador.

A API funciona como o **cérebro do sistema**, recebendo e respondendo a solicitações feitas pelos outros dois componentes.  
A página web e o aplicativo se comunicam com ela para exibir dados, enviar informações e realizar cálculos.

---

## 🧠 Como o Sistema Funciona

1. O **usuário** acessa o sistema pelo **aplicativo Android** ou pela **página web**.  
2. Quando ele faz uma ação (como visualizar clientes ou solicitar um cálculo), a interface envia uma requisição para a **API**.  
3. A **API** processa a solicitação e devolve uma resposta em formato simples (JSON).  
4. O **App** ou a **Web** exibe o resultado de forma amigável ao usuário.

Esse modelo garante rapidez, segurança e flexibilidade — qualquer atualização feita na API reflete automaticamente nas duas interfaces.

---

## 🔄 Diagrama de Blocos

```
          ┌───────────────────────┐
          │       Usuário         │
          │ (App ou Navegador)    │
          └──────────┬────────────┘
                     │
     ┌───────────────┼────────────────┐
     │                                │
     ▼                                ▼
┌───────────────┐               ┌────────────────┐
│ App Android   │               │ Página Web     │
│ (Kotlin)      │               │ (HTML + JS)    │
└──────┬────────┘               └──────┬─────────┘
       │                               │
       │  Requisições HTTP (GET/POST)  │
       │                               │
       ▼                               ▼
        ┌──────────────────────────────┐
        │          API Clojure         │
        │   (/clientes, /calculo)      │
        │     Responde com JSON        │
        └──────────────────────────────┘
```

Explicação Rápida

O usuário acessa o sistema pelo App Android ou pela Página Web.

Ambos se comunicam diretamente com a API desenvolvida em Clojure.

A API recebe requisições (como cálculos ou listagem de clientes) e devolve respostas em JSON, que são exibidas para o usuário.

---

## 🎯 Benefícios para o Cliente

- 💻 **Acesso multiplataforma:** o mesmo sistema pode ser usado no computador ou no celular.  
- ⚡ **Comunicação rápida:** a API garante respostas instantâneas.  
- 🔒 **Centralização de dados:** todas as informações são gerenciadas por um único núcleo.  
- 🌍 **Escalabilidade:** novas funcionalidades podem ser adicionadas sem precisar alterar o aplicativo ou o site.  

---

## 🧩 Possíveis Expansões Futuras

- Criação de área de login personalizada.  
- Dashboards e relatórios visuais.  
- Suporte a múltiplos usuários com permissões diferentes.  

---

📱 **App Android** — interface móvel para uso rápido e prático.  
🖥️ **Página Web** — acesso direto pelo navegador.  
⚙️ **API Clojure** — núcleo inteligente que conecta tudo.  
**Supabase** - Banco de Dados

---

📘 **Autor:** Murilo Molina  
📅 **Última atualização:** 2025  
