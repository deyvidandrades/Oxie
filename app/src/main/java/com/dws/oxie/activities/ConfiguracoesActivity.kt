package com.dws.oxie.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dws.oxie.R
import com.dws.oxie.assistentes.AnimacaoBotao

class ConfiguracoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_configuracoes)

        val btnVoltar = findViewById<ImageView>(R.id.btn_voltar)
        val btnExcluirDados = findViewById<TextView>(R.id.btn_excluir)

        btnVoltar.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            finish()
        }

        btnExcluirDados.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
        }
    }
}