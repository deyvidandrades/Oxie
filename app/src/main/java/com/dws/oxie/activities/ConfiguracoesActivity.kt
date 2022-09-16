package com.dws.oxie.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dws.oxie.R
import com.dws.oxie.assistentes.AnimacaoBotao
import com.dws.oxie.persistencia.Persistencia

class ConfiguracoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_configuracoes)

        val btnVoltar = findViewById<ImageView>(R.id.btn_voltar)
        val btnExcluirDados = findViewById<ImageView>(R.id.btn_excluir)
        val btnSalvarIP = findViewById<ImageView>(R.id.btn_salvar)
        val etEnderecoIP = findViewById<EditText>(R.id.et_endereco_ip)

        etEnderecoIP.setText(Persistencia(applicationContext).carregarIP())

        btnVoltar.setOnClickListener { view ->
            AnimacaoBotao.animar(view)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnExcluirDados.setOnClickListener { view ->
            AnimacaoBotao.animar(view)

            Persistencia(applicationContext).deletarDados("historico")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSalvarIP.setOnClickListener { view ->
            AnimacaoBotao.animar(view)
            Persistencia(this).salvarIP(etEnderecoIP.text.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}