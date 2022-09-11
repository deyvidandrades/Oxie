package com.dws.oxie.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dws.oxie.R
import com.dws.oxie.assistentes.AnimacaoBotao
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.net.NoRouteToHostException
import java.net.URL
import java.net.URLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var textBatimentos: TextView
    private lateinit var textOxigenacao: TextView
    private lateinit var textPassos: TextView

    private lateinit var progressBatimentos: CircularProgressIndicator
    private lateinit var progressOxigenacao: CircularProgressIndicator
    private lateinit var progressPassos: CircularProgressIndicator

    private var arrayAtualizacoes = arrayListOf(1, 1, 1)

    private lateinit var mainHandler: Handler

    private val updateTextTask = object : Runnable {
        override fun run() {
            arrayAtualizacoes.clear()
            arrayAtualizacoes = fazerRequisicaoRotas()
            Log.d("DWS.D", arrayAtualizacoes.toString())

            updateUI(arrayAtualizacoes)

            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        textBatimentos = findViewById(R.id.tv_batimentos)
        textOxigenacao = findViewById(R.id.tv_oxigenacao)
        textPassos = findViewById(R.id.tv_passos)

        progressBatimentos = findViewById(R.id.progress_batimentos)
        progressOxigenacao = findViewById(R.id.progress_oxigenacao)
        progressPassos = findViewById(R.id.progress_passos)

        val btnConectar = findViewById<RelativeLayout>(R.id.btn_conectar)
        val btnDesconectar = findViewById<RelativeLayout>(R.id.btn_desconectar)
        val btnMenu = findViewById<ImageView>(R.id.btn_menu)

        val btnHistoricoBatimentos = findViewById<LinearLayout>(R.id.li_historico_batimentos)
        val btnHistoricoOxigenacao = findViewById<LinearLayout>(R.id.li_historico_oxigenacao)
        val btnHistoricoPassos = findViewById<LinearLayout>(R.id.li_historico_passos)

        mainHandler = Handler(Looper.getMainLooper())

        btnConectar.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            mainHandler.post(updateTextTask)

            btnConectar.visibility = View.GONE
            btnDesconectar.visibility = View.VISIBLE
        }

        btnDesconectar.setOnClickListener { view ->
            mainHandler.removeCallbacks(updateTextTask)

            btnDesconectar.visibility = View.GONE
            btnConectar.visibility = View.VISIBLE
        }
        btnMenu.setOnClickListener { view ->
            AnimacaoBotao.animar(view)

            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }

        btnHistoricoBatimentos.setOnClickListener { view ->
            AnimacaoBotao.animar(view)

            val intent = Intent(this, HistoricoActivity::class.java)
            intent.putExtra("tipo", 0)
            startActivity(intent)

        }
        btnHistoricoOxigenacao.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            val intent = Intent(this, HistoricoActivity::class.java)
            intent.putExtra("tipo", 1)
            startActivity(intent)

        }
        btnHistoricoPassos.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            val intent = Intent(this, HistoricoActivity::class.java)
            intent.putExtra("tipo", 2)
            startActivity(intent)

        }
    }

    private fun fazerRequisicaoRotas(): ArrayList<Int> {
        val ip = "192.168.0.133"
        val porta = 80
        val array = arrayListOf(0, 0, 0)

        val urlConnection = URL("HTTP", ip, porta, "cu").openConnection() as URLConnection

        val thread = Thread {
            try {
                val res = urlConnection.inputStream.bufferedReader().readText().split(", ")
                array.clear()
                array.add(res[2].toInt())
                array.add(res[1].toInt())
                array.add(res[0].toInt())

            } catch (e: NoRouteToHostException) {
               // Toast.makeText(
               //     application.applicationContext,
               //     "Falha ao conectar com o dispositivo.",
               //     Toast.LENGTH_LONG
               // )
               //     .show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()
        thread.join(500)
        return array
    }

    private fun fazerRequisicao(debug: Boolean) {
        val ip = if (debug) "192.168.0.175" else "192.168.0.133"
        val porta = if (debug) 5000 else 80

        val url = URL("HTTP", ip, porta, "")
        val urlConnection = url.openConnection() as URLConnection

        val thread = Thread {
            try {
                val text = urlConnection.inputStream.bufferedReader().readText()

                val batimento = text.toInt()
                val oxigenacao = text.toInt()
                val passos = text.toInt()

                val regex = Regex("<span id=\"(.+?)\">(.*?)</span>")
                val matches = regex.findAll(text)

                arrayAtualizacoes.clear()

                matches.iterator().forEach { item ->
                    arrayAtualizacoes.add(item.groups[2]!!.value.toInt())
                }

            } catch (e: NoRouteToHostException) {
                Toast.makeText(
                    application.applicationContext,
                    "Falha ao conectar com o dispositivo.",
                    Toast.LENGTH_LONG
                )
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()
        thread.join()

        //updateUI(arrayAtualizacoes)
    }

    @SuppressLint("SetTextI18n")
    fun updateUI(arrayValores: ArrayList<Int>) {
        Log.d("DWS.D", arrayValores.toString())

        textBatimentos.text =
            if (arrayValores[0] > 0) "${arrayValores[0]}BPM" else textBatimentos.text

        textOxigenacao.text =
            if (arrayValores[1] > 0) "${arrayValores[1]}%" else textOxigenacao.text

        textPassos.text =
            if (arrayValores[2] > 0) "${arrayValores[2]}/6000" else textPassos.text

        progressBatimentos.progress = (
                (if (arrayValores[0] > 0) arrayValores[0] else progressBatimentos.progress) * 100
                ) / 110

        progressOxigenacao.progress = (
                (if (arrayValores[1] > 0) arrayValores[1] else progressOxigenacao.progress) * 100
                ) / 100

        progressPassos.progress = (
                (if (arrayValores[2] > 0) arrayValores[2] else progressPassos.progress) * 100
                ) / 6000
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(updateTextTask)
    }
}