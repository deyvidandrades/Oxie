package com.dws.oxie.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dws.mufla.adaptadores.AdaptadorItemHistorico
import com.dws.oxie.R
import com.dws.oxie.assistentes.AnimacaoBotao
import com.dws.oxie.objetos.ItemHistorico
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HistoricoActivity : AppCompatActivity() {
    private var arrayItemHistorico = ArrayList<ItemHistorico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_historico)

        val recyclerHistorico = findViewById<RecyclerView>(R.id.recycler_historico)
        val btn_voltar = findViewById<ImageView>(R.id.btn_voltar)
        val liBatimentos = findViewById<LinearLayout>(R.id.li_batimentos)
        val liOxigenacao = findViewById<LinearLayout>(R.id.li_oxigenacao)
        val liPassos = findViewById<LinearLayout>(R.id.li_passos)

        val tipo: Int = intent.getIntExtra("tipo", 0)

        val layoutManagerHorarioOnibus =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adaptadorHorarioOnibus = AdaptadorItemHistorico(this, arrayItemHistorico)

        recyclerHistorico.setHasFixedSize(true)
        recyclerHistorico.adapter = adaptadorHorarioOnibus
        recyclerHistorico.layoutManager = layoutManagerHorarioOnibus


        when (tipo) {
            1 -> {
                liBatimentos.visibility = View.GONE
                liOxigenacao.visibility = View.VISIBLE
                liPassos.visibility = View.GONE
            }
            2 -> {
                liBatimentos.visibility = View.GONE
                liOxigenacao.visibility = View.GONE
                liPassos.visibility = View.VISIBLE
            }
            else -> {
                liBatimentos.visibility = View.VISIBLE
                liOxigenacao.visibility = View.GONE
                liPassos.visibility = View.GONE
            }
        }

        btn_voltar.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            finish()
        }

        carregarHistorico()
    }


    private fun carregarHistorico() {
        val calendario = Calendar.getInstance()
        val df: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val data: String = df.format(calendario.time)

        arrayItemHistorico.add(ItemHistorico(data, 87.0, "bpm"))
        arrayItemHistorico.add(ItemHistorico(data, 74.0, "bpm"))
        arrayItemHistorico.add(ItemHistorico(data, 68.0, "bpm"))
        arrayItemHistorico.add(ItemHistorico(data, 120.0, "bpm"))
        arrayItemHistorico.add(ItemHistorico(data, 62.0, "bpm"))
    }
}