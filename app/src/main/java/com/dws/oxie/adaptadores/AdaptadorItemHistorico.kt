package com.dws.mufla.adaptadores

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dws.oxie.R
import com.dws.oxie.objetos.ItemHistorico

class AdaptadorItemHistorico(context: Context, arrayList: ArrayList<ItemHistorico>) :
    RecyclerView.Adapter<AdaptadorItemHistorico.ViewHolder>() {

    private val context: Context
    private var arrayList: ArrayList<ItemHistorico> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.item_historico, parent, false
        )
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arrayList[position]

        holder.tvData.text = item.getData()
        holder.tvValor.text = item.getValor().toString()
        holder.tvUnidade.text = item.getUnidade()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvData: TextView
        var tvValor: TextView
        var tvUnidade: TextView

        init {
            tvData = itemView.findViewById(R.id.tv_data)
            tvValor = itemView.findViewById(R.id.tv_valor)
            tvUnidade = itemView.findViewById(R.id.tv_unidade)
        }
    }

    init {
        this.context = context
        this.arrayList = arrayList
    }
}