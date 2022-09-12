package com.dws.oxie.adaptadores

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dws.oxie.R
import com.dws.oxie.objetos.ItemHistorico
import java.text.SimpleDateFormat
import java.util.*

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

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val data = simpleDateFormat.format(item.getData())

        holder.tvData.text = data
        holder.tvValorBatimentos.text = item.getValorBatimentos().toString()
        holder.tvValorOxigenacao.text = item.getValorOxigenacao().toString()
        holder.tvValorPassos.text = item.getValorPassos().toString()
        holder.tvValorPassosRaw.text = "${(item.getValorPassos() * 6000) / 100}/6000"
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvData: TextView
        var tvValorBatimentos: TextView
        var tvValorOxigenacao: TextView
        var tvValorPassos: TextView
        var tvValorPassosRaw: TextView

        init {
            tvData = itemView.findViewById(R.id.tv_data)
            tvValorBatimentos = itemView.findViewById(R.id.tv_valor_bpm)
            tvValorOxigenacao = itemView.findViewById(R.id.tv_valor_oxidacao)
            tvValorPassos = itemView.findViewById(R.id.tv_valor_passos)
            tvValorPassosRaw = itemView.findViewById(R.id.tv_valor_passos_raw)
        }
    }

    init {
        this.context = context
        this.arrayList = arrayList
    }
}