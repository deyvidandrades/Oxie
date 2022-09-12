package com.dws.oxie.persistencia

import android.content.Context
import android.util.Log
import com.dws.oxie.objetos.ItemHistorico
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class Persistencia(private val context: Context) {
    @Suppress("PrivatePropertyName")
    private val PREFERENCE_STRING = "OXIE_HISTORICO"

    fun carregarDados(key: String): ArrayList<ItemHistorico> {
        val listaRaw = context.getSharedPreferences(PREFERENCE_STRING, Context.MODE_PRIVATE)
            .getString(key, "")!!

        return try {
            val type: Type = object : TypeToken<ArrayList<ItemHistorico>>() {}.type
            val lista: ArrayList<ItemHistorico> = Gson().fromJson(listaRaw, type)
            lista.sortWith(Collections.reverseOrder())

            lista
        } catch (e: NullPointerException) {
            ArrayList()
        }
    }

    fun salvarDados(key: String, lista: ArrayList<ItemHistorico>) {
        val listaOld = ArrayList<ItemHistorico>()

        listaOld.addAll(carregarDados(key))
        listaOld.addAll(lista)

        val listaJSONString = Gson().toJson(listaOld)
        with(context.getSharedPreferences(PREFERENCE_STRING, Context.MODE_PRIVATE).edit()) {
            putString(key, listaJSONString)
            commit()
        }
    }

    fun deletarDados(key: String) {
        with(context.getSharedPreferences(PREFERENCE_STRING, Context.MODE_PRIVATE).edit()) {
            putString(key, "")
            commit()
        }
    }
}