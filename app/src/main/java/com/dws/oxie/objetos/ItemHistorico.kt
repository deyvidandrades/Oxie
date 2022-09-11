package com.dws.oxie.objetos

data class ItemHistorico(
    private var data: String?,
    private var valor: Double,
    private var unidade: String?
) : Comparable<ItemHistorico> {

    fun getData() = data
    fun getValor() = valor
    fun getUnidade() = unidade

    override operator fun compareTo(other: ItemHistorico): Int {
        return getData()!!.compareTo(other.getData()!!)
    }

    override fun toString(): String {
        return "ItemHistorico(data=$data, valor=$valor, unidade=$unidade)"
    }
}