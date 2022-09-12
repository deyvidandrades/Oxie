package com.dws.oxie.objetos

data class ItemHistorico(
    private var data: Long?,
    private var valorBatimentos: Int,
    private var valorOxigenacao: Int,
    private var valorPassos: Int
) : Comparable<ItemHistorico> {

    fun getData() = data
    fun getValorBatimentos() = valorBatimentos
    fun getValorOxigenacao() = valorOxigenacao
    fun getValorPassos() = valorPassos

    override operator fun compareTo(other: ItemHistorico): Int {
        return getData()!!.compareTo(other.getData()!!)
    }

    override fun toString(): String {
        return "ItemHistorico(data=$data, valorBatimentos=$valorBatimentos, valorOxigenacao=$valorOxigenacao, valorPassos=$valorPassos)"
    }
}