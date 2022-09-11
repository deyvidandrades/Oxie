package com.dws.oxie.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
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

    private var arrayAtualizacoes = ArrayList<Double>()

    private lateinit var mainHandler: Handler
    private val updateTextTask = object : Runnable {
        override fun run() {
            fazerRequisicao(true)
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

    private fun fazerRequisicao(debug: Boolean) {
        val ip = if (debug) "192.168.0.175" else "192.168.0.133"
        val porta = if (debug) 5000 else 80

        val url = URL("HTTP", ip, porta, "")
        val urlConnection = url.openConnection() as URLConnection

        val thread = Thread {
            try {
                val text = urlConnection.inputStream.bufferedReader().readText()

                val regex = Regex("<span id=\"(.+?)\">(.*?)</span>")
                val matches = regex.findAll(text)

                arrayAtualizacoes.clear()

                matches.iterator().forEach { item ->
                    arrayAtualizacoes.add(item.groups[2]!!.value.toDouble())
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

        updateUI(arrayAtualizacoes)
    }

    @SuppressLint("SetTextI18n")
    fun updateUI(arrayValores: ArrayList<Double>) {
        Log.d("DWS.D", arrayValores.toString())

        textBatimentos.text = "${arrayValores[0].toInt()}/80"
        textOxigenacao.text = "${arrayValores[1].toInt()}%"
        textPassos.text = "${arrayValores[2].toInt()}/6000"

        progressBatimentos.progress = ((arrayValores[0] * 100) / 150).toInt()
        progressOxigenacao.progress = ((arrayValores[1] * 100) / 100).toInt()
        progressPassos.progress = ((arrayValores[2] * 100) / 6000).toInt()
    }

    /*override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }*/

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(updateTextTask)
    }
}