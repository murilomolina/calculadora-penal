# 🧩 Projeto Integrado — API + App Android + Página Web

## 📖 Visão Geral

Este projeto é composto por três partes integradas:

1. **API (Clojure / Leiningen)** — responsável por servir e processar dados, incluindo rotas como `/clientes` e `/calculo`.  
2. **Aplicativo Android (Kotlin)** — consome a API para exibir e enviar informações.  
3. **Página Web (HTML/JavaScript)** — interface simples para acessar as mesmas rotas da API diretamente do navegador.

A comunicação entre os três sistemas é feita via **HTTP**, e a **API** utiliza **CORS** configurado para permitir conexões locais (por exemplo, do `http://127.0.0.1:5500` e do App Android).

---

## ⚙️ Estrutura do Projeto

```
projeto/
├── api/
│   ├── project.clj
│   └── src/api/core.clj
│
├── app-android/
│   ├── app/src/main/java/com/example/app/
│   └── ...
│
└── web/
    ├── index.html
    ├── script.js
    └── style.css
```

---

## 🚀 1. API (Clojure)


### ▶️ Execução
```bash
cd api
lein run
```

A API iniciará em:
```
http://localhost:8080
```

### 🧠 Rotas disponíveis
| Método | Endpoint        | Descrição                       | Exemplo de retorno |
|---------|----------------|----------------------------------|--------------------|
| GET     | `/clientes`    | Lista clientes cadastrados       | `[{"id":1,"nome":"Murilo"}]` |
| POST    | `/calculo`     | Retorna cálculo de um valor enviado | `{"resultado": 10}` |

---

## 📱 2. Aplicativo Android

### 🧩 Descrição
O aplicativo Android é desenvolvido em **Kotlin**, usando **Android Studio**.  
Ele realiza requisições HTTP à API para listar clientes e enviar valores para o endpoint `/calculo`.



> **Observação:**  
> Se for testar no dispositivo físico, altere o IP da `ipAPI` para o IP local da sua máquina na rede (ex: `http://192.168.0.10:8080/`).

---

## 💻 3. Página Web


---

## 🧠 Comunicação entre sistemas

| Origem | Destino | Protocolo | Descrição |
|--------|----------|------------|------------|
| Página Web (`127.0.0.1:5500`) | API (`localhost:8080`) | HTTP (CORS) | Requisições diretas via fetch |
| App Android | API (`10.0.2.2:8080`) | HTTP
| API | Banco de dados (supabase) 

---

## 🧰 Tecnologias Utilizadas

- **Clojure + Leiningen** — Backend e rotas REST.  
- **Ring + Compojure** — Estrutura de rotas e servidor.  
- **ring-cors** — Middleware de controle de acesso.  
- **Kotlin** — Android.  
- **HTML + JavaScript** — Interface web simples para testes.  
- **SUPABASE** — Banco de Dados

---

## 🔒 Observações Importantes

- Mantenha o middleware `wrap-cors` **como o mais externo** da aplicação.  
- Durante desenvolvimento, o CORS está liberado apenas para `127.0.0.1:5500`.  
- Para produção, configure o domínio correto do seu front-end.  
- O Android deve usar o IP da máquina local (ex: `10.0.2.2`) para acessar a API.  

---

## 🧩 Próximos Passos

- Criar autenticação de usuário (JWT ou OAuth2).  
- Melhorar interface web e app mobile.  
- Adicionar testes automatizados.

---

🛠️ **Autor:** Murilo Molina  
📅 **Última atualização:** 2025  
