package com.dws.oxie.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dws.oxie.R
import com.dws.oxie.adaptadores.AdaptadorItemHistorico
import com.dws.oxie.assistentes.AnimacaoBotao
import com.dws.oxie.objetos.ItemHistorico
import com.dws.oxie.persistencia.Persistencia

class HistoricoActivity : AppCompatActivity() {
    private var arrayItemHistorico = ArrayList<ItemHistorico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_historico)

        val recyclerHistorico = findViewById<RecyclerView>(R.id.recycler_historico)
        val btnVoltar = findViewById<ImageView>(R.id.btn_voltar)

        val layoutManagerHorarioOnibus =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adaptadorHorarioOnibus = AdaptadorItemHistorico(this, arrayItemHistorico)

        recyclerHistorico.setHasFixedSize(true)
        recyclerHistorico.adapter = adaptadorHorarioOnibus
        recyclerHistorico.layoutManager = layoutManagerHorarioOnibus

        btnVoltar.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            finish()
        }

        carregarHistorico()
    }

    private fun carregarHistorico() {
        val persistencia = Persistencia(applicationContext)
        val dados = persistencia.carregarDados("historico")

        arrayItemHistorico.addAll(dados)
    }
}