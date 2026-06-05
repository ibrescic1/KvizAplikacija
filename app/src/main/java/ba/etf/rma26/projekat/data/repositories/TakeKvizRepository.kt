package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.repositories.ApiConfig
import ba.etf.rma26.projekat.data.models.KvizTaken

object TakeKvizRepository {
    private val api get() = ApiConfig.service

    suspend fun zapocniKviz(idKviza: Int): KvizTaken? {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isBlank()) return null
            api.zapocniKviz(hash, idKviza)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getPocetiKvizovi(): List<KvizTaken>? {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isBlank()) return null
            api.getPocetiKvizovi(hash)
        } catch (e: Exception) {
            null
        }
    }
}