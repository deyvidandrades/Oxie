package com.dws.oxie.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dws.oxie.R
import com.dws.oxie.adaptadores.AdaptadorItemHistorico
import com.dws.oxie.assistentes.AnimacaoBotao
import com.dws.oxie.objetos.ItemHistorico
import com.dws.oxie.persistencia.Persistencia
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.net.URL
import java.net.URLConnection
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var textBatimentos: TextView
    private lateinit var textOxigenacao: TextView
    private lateinit var textPassos: TextView
    private lateinit var ip: List<String>

    private lateinit var progressBatimentos: CircularProgressIndicator
    private lateinit var progressOxigenacao: CircularProgressIndicator
    private lateinit var progressPassos: CircularProgressIndicator

    private lateinit var btnConectar: ImageView

    private val debug: Boolean = false
    private var erroRede = false

    private var arrayItemHistorico = ArrayList<ItemHistorico>()
    private lateinit var adaptadorHorarioOnibus: AdaptadorItemHistorico

    private lateinit var mainHandler: Handler
    private val updateTextTask = object : Runnable {
        override fun run() {
            if (!erroRede) {
                ip = Persistencia(applicationContext).carregarIP().split(":")
                Log.d("DWS.D", ip.toString())

                if (ip.size > 1) {
                    val arrayAtualizacoes = fazerRequisicao()

                    if (arrayAtualizacoes[0] > 0) {
                        updateUI(arrayAtualizacoes)
                        salvarDados(arrayAtualizacoes)
                        carregarHistorico()
                    }

                    mainHandler.postDelayed(this, 1000)
                }

                btnConectar.setColorFilter(getColor(R.color.accentBlue))
            } else {
                btnConectar.setColorFilter(getColor(R.color.textLight))

                Toast.makeText(
                    this@MainActivity, "Falha na conexão", Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContentView(R.layout.activity_main)

        textBatimentos = findViewById(R.id.tv_batimentos)
        textOxigenacao = findViewById(R.id.tv_oxigenacao)
        textPassos = findViewById(R.id.tv_passos)

        progressBatimentos = findViewById(R.id.progress_batimentos)
        progressOxigenacao = findViewById(R.id.progress_oxigenacao)
        progressPassos = findViewById(R.id.progress_passos)

        mainHandler = Handler(Looper.getMainLooper())

        val recyclerHistorico = findViewById<RecyclerView>(R.id.recycler_historico)

        val layoutManagerHorarioOnibus =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adaptadorHorarioOnibus = AdaptadorItemHistorico(this, arrayItemHistorico)

        recyclerHistorico.setHasFixedSize(true)
        recyclerHistorico.adapter = adaptadorHorarioOnibus
        recyclerHistorico.layoutManager = layoutManagerHorarioOnibus

        configurarBotoes()
        carregarHistorico()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun carregarHistorico() {
        val persistencia = Persistencia(applicationContext)
        val dados = persistencia.carregarDados("historico")
        val reHistorico = findViewById<RelativeLayout>(R.id.re_historico)

        arrayItemHistorico.clear()

        if (dados.size > 0) {
            reHistorico.visibility = View.VISIBLE

            updateUI(
                arrayListOf(
                    dados[0].getValorBatimentos(),
                    dados[0].getValorOxigenacao(),
                    (dados[0].getValorPassos() * 6000) / 100
                )
            )

            arrayItemHistorico.add(dados[0])

            if (dados.size > 1)
                arrayItemHistorico.add(dados[1])

            adaptadorHorarioOnibus.notifyDataSetChanged()
        } else
            reHistorico.visibility = View.GONE
    }

    private fun configurarBotoes() {
        btnConectar = findViewById(R.id.btn_conectar)
        val btnMenu = findViewById<ImageView>(R.id.btn_menu)
        val btnVerTodos = findViewById<TextView>(R.id.tv_ver_todos)

        btnConectar.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            mainHandler.post(updateTextTask)
            erroRede = false
        }

        btnMenu.setOnClickListener { view ->
            AnimacaoBotao.animar(view)

            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnVerTodos.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            val intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fazerRequisicao(): ArrayList<Int> {
        val enderecoIp = ip[0] //if (debug) "192.168.0.175" else "192.168.0.133"
        val porta = ip[1].toInt() //if (debug) 5000 else 80
        val array = arrayListOf(0, 0, 0)

        val urlConnection = URL(
            "HTTP",
            enderecoIp,
            porta,
            if (debug) "" else "cu"
        ).openConnection() as URLConnection

        val thread = Thread {
            try {
                val res = urlConnection.inputStream.bufferedReader().readText().split(", ")
                array.clear()
                array.add(res[2].toInt())
                array.add(res[1].toInt())
                array.add(res[0].toInt())

            } catch (e: Exception) {
                erroRede = true
            }
        }

        thread.start()
        thread.join(1000)

        return array
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(arrayValores: ArrayList<Int>) {

        textBatimentos.text =
            if (arrayValores[0] > 0) "${arrayValores[0]}" else textBatimentos.text

        textOxigenacao.text =
            if (arrayValores[1] > 0) "${arrayValores[1]}%" else textOxigenacao.text

        textPassos.text =
            if (arrayValores[2] > 0) "${arrayValores[2]}/6000" else textPassos.text

        progressBatimentos.progress =
            if (arrayValores[0] > 0) (arrayValores[0] * 100) / 110 else progressBatimentos.progress

        progressOxigenacao.progress =
            if (arrayValores[1] > 0) (arrayValores[1] * 100) / 100 else progressOxigenacao.progress

        progressPassos.progress =
            if (arrayValores[2] > 0) (arrayValores[2] * 100) / 6000 else progressPassos.progress
    }

    fun salvarDados(arrayValores: ArrayList<Int>) {
        val item = ItemHistorico(
            Calendar.getInstance().timeInMillis,
            arrayValores[0],
            arrayValores[1],
            (arrayValores[2] * 100) / 6000
        )

        Persistencia(applicationContext).salvarDados("historico", arrayListOf(item))
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(updateTextTask)
    }
}