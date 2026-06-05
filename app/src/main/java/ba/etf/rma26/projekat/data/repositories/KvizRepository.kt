package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.repositories.ApiConfig
import ba.etf.rma26.projekat.data.models.Kviz

object KvizRepository {
    private val api get() = ApiConfig.service

    suspend fun getAll(): List<Kviz> = try {
        api.getKvizovi()
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun getById(id: Int): Kviz? = try {
        api.getKvizById(id)
    } catch (e: Exception) {
        null
    }

    suspend fun getUpisani(): List<Kviz> {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isBlank()) return emptyList()
            api.getUpisaniKvizovi(hash)
        } catch (e: Exception) {
            emptyList()
        }
    }
}