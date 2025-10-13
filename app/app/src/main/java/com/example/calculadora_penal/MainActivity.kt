package com.example.calculadora_penal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// new imports
import com.google.gson.GsonBuilder
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.google.android.material.textfield.TextInputEditText
import android.content.Intent // pra abrir o link do zap
import android.net.Uri // link do zap
import android.widget.Toast
// imports da funcao de post
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

//debug
import android.util.Log

data class UserInput(
    val penaTotalAnos: String,
    val penaTotalMeses: String,
    val penaTotalDias: String,
    val inicioCumprimento: String,
    val detracaoAnos: String,
    val detracaoMeses: String,
    val detracaoDias: String,
    val tipoCrime: String,
    val statusApenado: String,
)
class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // forca o modo escuro sempre
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // new (line 26)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //////////////////////////////////////////
        val penaTotalAnos = findViewById<NumberPicker>(R.id.penaTotalAnosPicker)
        val penaTotalMeses = findViewById<NumberPicker>(R.id.penaTotalMesesPicker)
        val penaTotalDias = findViewById<NumberPicker>(R.id.penaTotalDiasPicker)

        // Configurar limites
        penaTotalAnos.minValue = 0
        penaTotalAnos.maxValue = 50
        penaTotalAnos.wrapSelectorWheel = false
        penaTotalAnos.displayedValues = null   // limpa qualquer valor antigo
        penaTotalAnos.displayedValues = Array(51) { i -> "$i anos" }

        penaTotalMeses.minValue = 0
        penaTotalMeses.maxValue = 11
        penaTotalMeses.wrapSelectorWheel = true
        penaTotalMeses.displayedValues = null   // limpa qualquer valor antigo
        penaTotalMeses.displayedValues = Array(12) { i -> "$i meses" }

        penaTotalDias.minValue = 0
        penaTotalDias.maxValue = 30
        penaTotalDias.wrapSelectorWheel = true
        penaTotalDias.displayedValues = null   // limpa qualquer valor antigo
        penaTotalDias.displayedValues = Array(penaTotalDias.maxValue - penaTotalDias.minValue + 1) { i -> "$i dias" }
        // Rótulos abaixo dos números (opcional, só no texto)
        penaTotalAnos.contentDescription = "anos"
        penaTotalMeses.contentDescription = "meses"
        penaTotalDias.contentDescription = "dias"


        /////////////////////////////////////
        val inicioCumprimento = findViewById< TextInputEditText>(R.id.inicioCumprimento)

        inicioCumprimento.keyListener = null
        inicioCumprimento.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val datePicker = android.app.DatePickerDialog(this,
                { _, year, month, day ->
                    val dataFormatada = String.format("%02d-%02d-%04d", day, month + 1, year)
                    inicioCumprimento.setText(dataFormatada)
                },
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }


        /////////////////////////////////////
        val detracaoAnos = findViewById<NumberPicker>(R.id.detracaoAnosPicker)
        val detracaoMeses = findViewById<NumberPicker>(R.id.detracaoMesesPicker)
        val detracaoDias = findViewById<NumberPicker>(R.id.detracaoDiasPicker)

        // Configurar limites
        detracaoAnos.minValue = 0
        detracaoAnos.maxValue = 50
        detracaoAnos.wrapSelectorWheel = false
        detracaoAnos.displayedValues = null
        detracaoAnos.displayedValues = Array(51) { i -> "$i anos" }

        detracaoMeses.minValue = 0
        detracaoMeses.maxValue = 11
        detracaoMeses.wrapSelectorWheel = true
        detracaoMeses.displayedValues = null
        detracaoMeses.displayedValues = Array(12) { i -> "$i meses" }

        detracaoDias.minValue = 0
        detracaoDias.maxValue = 30
        detracaoDias.wrapSelectorWheel = true
        detracaoDias.displayedValues = null
        detracaoDias.displayedValues = Array(31) { i -> "$i dias" }
        // Rótulos abaixo dos números (opcional, só no texto)
        detracaoAnos.contentDescription = "anos"
        detracaoMeses.contentDescription = "meses"
        detracaoDias.contentDescription = "dias"


        ////////////////////////////////////////
        val tipoCrimeSpinner = findViewById<Spinner>(R.id.tipoCrimeSpinner)

        // Lista de opções
        val opcoesTipoCrime = arrayOf("comum", "hediondo")

        // Adapter para o Spinner
        val adapterTipoCrime = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoesTipoCrime)
        adapterTipoCrime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoCrimeSpinner.adapter = adapterTipoCrime

        // Para ler a opção selecionada
//        val tipoCrimeSelecionado = tipoCrimeSpinner.selectedItem.toString()
//        Log.d("DEBUG", "Tipo de crime selecionado: $tipoCrimeSelecionado")

        //////////////////////////////////////////
        val statusApenadoSpinner = findViewById<Spinner>(R.id.statusApenadoSpinner)

        // Lista de opções
        val opcoesStatusApenado = arrayOf("primario", "reincidente")

        // Adapter para o Spinner
        val adapterStatusApenado = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoesStatusApenado)
        adapterStatusApenado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusApenadoSpinner.adapter = adapterStatusApenado

        // Para ler a opção selecionada
        // val statusSelecionado = statusApenadoSpinner.selectedItem.toString()




        ////////////////////////////////////// botao de calcular
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)

//        val outputText = findViewById<TextView>(R.id.outputText)

        val gson = GsonBuilder().setPrettyPrinting().create()

        btnGenerate.setOnClickListener {



            val data = UserInput(
                penaTotalAnos = penaTotalAnos.value.toString(),
                penaTotalMeses = penaTotalMeses.value.toString(),
                penaTotalDias = penaTotalDias.value.toString(),
                inicioCumprimento = inicioCumprimento.text.toString(),
                detracaoAnos = detracaoAnos.value.toString(),
                detracaoMeses = detracaoMeses.value.toString(),
                detracaoDias = detracaoDias.value.toString(),
                tipoCrime = tipoCrimeSpinner.selectedItem.toString(),
                statusApenado = statusApenadoSpinner.selectedItem.toString()
            )

            val penaTotalFinal = "${data.penaTotalAnos}, ${data.penaTotalMeses}, ${data.penaTotalDias}"
            val detracaoTotal = "${data.detracaoAnos}, ${data.detracaoMeses}, ${data.detracaoDias}"


            when {
                data.inicioCumprimento.isBlank() && ((data.penaTotalDias == "0") && (data.penaTotalMeses == "0") && (data.penaTotalAnos == "0")) -> {
                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Campos obrigatórios")
                        .setMessage("Por favor, preencha o campo de início de cumprimento e o campo de pena total antes de continuar.")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                    return@setOnClickListener
                }

                data.inicioCumprimento.isBlank() -> {
                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Campo obrigatório")
                        .setMessage("Por favor, preencha o campo de início de cumprimento antes de continuar.")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                    return@setOnClickListener
                }

                (data.penaTotalDias == "0" && data.penaTotalMeses == "0" && data.penaTotalAnos == "0") -> {
                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Campo obrigatório")
                        .setMessage("Por favor, preencha o campo de pena total antes de continuar.")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                    return@setOnClickListener
                }
            }

            val dataTratada = mapOf(
                "penaTotal" to penaTotalFinal,
                "inicioCumprimento" to data.inicioCumprimento,
                "tempoDetracao" to detracaoTotal,
                "tipoCrime" to data.tipoCrime,
                "statusApenado" to data.statusApenado
            )

            val json = gson.toJson(dataTratada)
//            outputText.text = json

            sendJsonToApi(json)
        }
    }



    private fun enviarWhatsApp() {
        val url = "https://api.whatsapp.com/send/?phone=5511989498044&text=Ol%C3%A1%2C+quero+tirar+duvidas+sobre+o+resultado+obtido+na+calculadora+penal%3F&type=phone_number&app_absent=0"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp não encontrado", Toast.LENGTH_SHORT).show()
        }
    }



    private fun enviarServidor(email: String, numero: String, processo: String, onFinished: (() -> Unit)? = null) {
        val data = mapOf(
            "email" to email,
            "numero" to numero,
            "processo" to processo
        )

        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(data)

        sendClientInfoAPI(json, onFinished)
    }


    private fun showSendToServerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_send_server, null)
        val etEmail = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etEmail)
        val etNumero = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etNumero)
        val etProcesso = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etProcesso)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Enviar informações")
            .setView(dialogView)
            .setPositiveButton("Enviar", null) // listener setado depois para controlar validação
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val btnEnviar = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            btnEnviar.setOnClickListener {
                val email = etEmail.text?.toString()?.trim() ?: ""
                val numero = etNumero.text?.toString()?.trim() ?: ""
                val processo = etProcesso.text?.toString()?.trim() ?: ""

                // Validações simples
                var valid = true
                if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.error = "E-mail inválido"
                    valid = false
                } else {
                    etEmail.error = null
                }

                // número: remove espaços e caracteres não numéricos (exceto +)
                val numeroNormalized = numero.replace("""[^\d+]""".toRegex(), "")
                if (numeroNormalized.isBlank() || numeroNormalized.length < 10) {
                    etNumero.error = "Número inválido"
                    valid = false
                } else {
                    etNumero.error = null
                }

                if (processo.isBlank()) {
                    etProcesso.error = "Informe o número do processo"
                    valid = false
                } else {
                    etProcesso.error = null
                }

                if (!valid) return@setOnClickListener

                // Desativa botão para evitar envios duplicados
                btnEnviar.isEnabled = false

                // Chama função que prepara JSON e envia
                enviarServidor(email, numeroNormalized, processo)
            }
        }

        dialog.show()
    }


    private fun showResultadoBottomSheet(jsonBody: String) {
        // Executa na UI thread
        runOnUiThread {
            try {
                val jsonObj = org.json.JSONObject(jsonBody)

                val penaTotalDias = jsonObj.optDouble("pena_total_dias", 0.0).toInt()
                val diasSemiAberto = jsonObj.optDouble("dias_p_semi_aberto", 0.0).toInt()
                val diasAberto = jsonObj.optDouble("dias_p_aberto", 0.0).toInt()
                val diasLivramento = jsonObj.optDouble("dias_p_livramento_condicional", 0.0).toInt()
                val inicioCumprimento = jsonObj.optString("inicio_Cumprimento", "")
                val dataSemiAberto = jsonObj.optString("data_inicio_semi_aberto", "")
                val dataAberto = jsonObj.optString("data_inicio_aberto", "")
                val dataLivramento = jsonObj.optString("data_inicio_livramento_condicional", "")

                // Mensagem formatada (pode personalizar)
                val mensagem = StringBuilder()
                    .append("📅 Início do cumprimento: ").append(if (inicioCumprimento.isNotBlank()) inicioCumprimento else "—").append("\n\n")
                    .append("• Pena total: ").append(penaTotalDias).append(" dias\n")
                    .append("• Progressão para semiaberto: ").append(dataSemiAberto).append(" (após ").append(diasSemiAberto).append(" dias)\n")
                    .append("• Progressão para aberto: ").append(dataAberto).append(" (após ").append(diasAberto).append(" dias)\n")
                    .append("• Livramento condicional: ").append(dataLivramento).append(" (após ").append(diasLivramento).append(" dias)\n")
                    .toString()

                val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.popup_resultado, null)
                dialog.setContentView(view)

                val txtResultado = view.findViewById<TextView>(R.id.txtResultadoDetalhado)
//                val btnFechar = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnFecharSheet)
                val btnContato = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnContato)
                val btnCompartilhar = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCompartilhar)

                txtResultado.text = mensagem

//                btnFechar.setOnClickListener { dialog.dismiss() }

                btnContato.setOnClickListener {
                    val options = arrayOf("Enviar mensagem pelo WhatsApp", "Esperar um advogado entrar em contato")

                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Como você quer prosseguir:")
                        .setItems(options) { dialog, which ->
                            when (which) {
                                0 -> {
                                    // Opção WhatsApp
                                    enviarWhatsApp()
                                }
                                1 -> {
                                    // Opção servidor
                                    showSendToServerDialog()
                                }
                            }
                        }
                        .show()
                }


                btnCompartilhar.setOnClickListener {
                    // Compartilhar texto via intent
                    val shareIntent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, mensagem)
                        type = "text/plain"
                    }
                    startActivity(android.content.Intent.createChooser(shareIntent, "Compartilhar resultado"))
                }

                // permitir que o sheet se expanda um pouco por padrão
                val bottomSheet = dialog.behavior
                bottomSheet.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
                dialog.show()

            } catch (e: Exception) {
                // fallback simples se parse falhar
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Resultado")
                    .setMessage("Resposta inválida do servidor.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }


//    private fun sendJsonToApi(json: String, outputView: TextView) {
    private fun sendJsonToApi(json: String) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val client = OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()


        val request = Request.Builder()
            .url("http://192.168.15.10:8080/calculo")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                     Log.d("DEBUG", "ERRO: ${e.message}")
//                    outputView.text = "${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string() // ler antes do runOnUiThread

                runOnUiThread {
                    if (response.isSuccessful && bodyString != null) {
                        // mostra o bottom sheet com o json
                        showResultadoBottomSheet(bodyString)
                    } else {
                        // exibe erro (pode usar AlertDialog)
                        androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                            .setTitle("Erro")
                            .setMessage("Código ${response.code}")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }
        })
    }


    private fun sendClientInfoAPI(json: String, onFinished: (() -> Unit)? = null) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://192.168.15.10:8080/clientes") // endpoint para captar contatos
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    // Mostra erro como Toast
                    Toast.makeText(this@MainActivity, "Erro ao enviar: ${e.message}", Toast.LENGTH_LONG).show()
                    // ou AlertDialog
                    /*
                    androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                        .setTitle("Erro")
                        .setMessage("Erro ao enviar: ${e.message}")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                    */
                    onFinished?.invoke()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = try { response.body?.string() } catch (e: Exception) { null }
                runOnUiThread {
                    if (response.isSuccessful) {
//                        Toast.makeText(this@MainActivity, "Enviado com sucesso!\n${body ?: ""}", Toast.LENGTH_LONG).show()
//                        // ou AlertDialog

                        androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                            .setTitle("Sucesso")
                            .setMessage("Enviado com sucesso!\n${body ?: ""}")
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()

                    } else {
//                        Toast.makeText(this@MainActivity, "Erro: ${response.code}\n${body ?: ""}", Toast.LENGTH_LONG).show()
                        // ou AlertDialog

                        androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                            .setTitle("Erro")
                            .setMessage("Erro: ${response.code}\n${body ?: ""}")
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()

                    }
                    onFinished?.invoke()
                }
            }
        })
    }
}