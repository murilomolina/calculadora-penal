document.getElementById("calcForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const anos = +document.getElementById("anos").value || 0;
  const meses = +document.getElementById("meses").value || 0;
  const dias = +document.getElementById("dias").value || 0;

  const detAnos = +document.getElementById("det_anos").value || 0;
  const detMeses = +document.getElementById("det_meses").value || 0;
  const detDias = +document.getElementById("det_dias").value || 0;

  const payload = {
    penaTotal: `${anos}, ${meses}, ${dias}`,
    inicioCumprimento: document.getElementById("inicio").value,
    tempoDetracao: `${detAnos}, ${detMeses}, ${detDias}`,
    tipoCrime: document.getElementById("crime").value,
    statusApenado: document.getElementById("status").value,
  };

  try {
    const response = await fetch("http://10.129.225.45:8080/calculo", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      throw new Error(`Erro: ${response.status} ${response.statusText}`);
    }

    const data = await response.json();
    alert("Resultado recebido da API:\n" + JSON.stringify(data, null, 2));
  } catch (err) {
    alert("Erro ao enviar requisição:\n" + err.message);
  }
});
