// Navegação simples entre páginas
const navButtons = document.querySelectorAll(".nav-btn");
const pages = document.querySelectorAll(".page");

navButtons.forEach(btn => {
  btn.addEventListener("click", () => {
    const pageId = btn.getAttribute("data-page");
    pages.forEach(p => p.classList.remove("active"));
    document.getElementById(pageId).classList.add("active");
    navButtons.forEach(b => b.classList.remove("active"));
    btn.classList.add("active");
  });
});

// --- Envio de cálculo ---
document.getElementById("calcForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const anos = parseInt(document.getElementById("anos").value) || 0;
  const meses = parseInt(document.getElementById("meses").value) || 0;
  const dias = parseInt(document.getElementById("dias").value) || 0;

  const detAnos = parseInt(document.getElementById("det_anos").value) || 0;
  const detMeses = parseInt(document.getElementById("det_meses").value) || 0;
  const detDias = parseInt(document.getElementById("det_dias").value) || 0;


  const inicioCumprimentoRaw = document.getElementById("inicio").value;
  const [inicioAnos, inicioMeses, inicioDias] = inicioCumprimentoRaw.split("-");

  const payload = {
    penaTotal: `${anos}, ${meses}, ${dias}`,
    inicioCumprimento: `${inicioDias}-${inicioMeses}-${inicioAnos}`,
    tempoDetracao: `${detAnos}, ${detMeses}, ${detDias}`,
    tipoCrime: document.getElementById("crime").value,
    statusApenado: document.getElementById("status").value,
  };

  try {
  const response = await fetch("http://localhost:8080/calculo", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!response.ok) throw new Error(`Erro: ${response.status} ${response.statusText}`);

  const data = await response.json();

  // Montar HTML bonito
  const resultadoBox = document.getElementById("resultadoBox");
  const resultadoConteudo = document.getElementById("resultadoConteudo");

  // Gera HTML formatado a partir do objeto retornado
  resultadoConteudo.innerHTML = Object.entries(data)
    .map(([chave, valor]) => `
      <div class="resultado-item">
        <span class="chave">${chave}</span>:
        <span class="valor">${valor}</span>
      </div>
    `).join("");

  resultadoBox.style.display = "block";
  resultadoBox.scrollIntoView({ behavior: "smooth" });

} catch (err) {
  alert("Erro ao enviar requisição:\n" + err.message);
}

});

// --- Enviar dados do cliente (POST para /clientes) ---
document.getElementById("enviarCliente").addEventListener("click", async () => {
  const email = document.getElementById("emailCliente").value.trim();
  const numero = document.getElementById("numeroCliente").value.trim();
  const processo = document.getElementById("processoCliente").value.trim();

  if (!email || !numero || !processo) {
    alert("Por favor, preencha todos os campos (e-mail, número e processo).");
    return;
  }

  const payload = { email, numero, processo };

  try {
    const response = await fetch("http://localhost:8080/clientes", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (!response.ok) throw new Error("Erro ao enviar os dados.");

    const data = await response.json();
    alert("Cliente cadastrado com sucesso:\n" + JSON.stringify(data, null, 2));

    document.getElementById("emailCliente").value = "";
    document.getElementById("numeroCliente").value = "";
    document.getElementById("processoCliente").value = "";
  } catch (err) {
    alert("Erro ao enviar:\n" + err.message);
  }
});


// --- Buscar clientes ---
document.getElementById("loadClientes").addEventListener("click", async () => {
  const container = document.getElementById("clientesList");
  container.innerHTML = "<p>Carregando...</p>";

  try {
    const response = await fetch("http://localhost:8080/clientes");
    const data = await response.json();

    if (!Array.isArray(data) || data.length === 0) {
      container.innerHTML = "<p>Nenhum cliente encontrado.</p>";
      return;
    }

    container.innerHTML = data.map(c => `
      <div class="cliente-card">
        <strong>Cliente ${c.id}</strong><br>
        <small>email: ${c.email || "N/A"}</small><br>
        <small>numero: ${c.numero || "N/A"}</small><br>
        <small>processo: ${c.processo || "N/A"}</small><br>
        <small>Criado em: ${c.created_at || "N/A"}</small><br>
      </div>
    `).join("");
  } catch (err) {
    container.innerHTML = `<p class="error">Erro: ${err.message}</p>`;
  }
});
