package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.repositories.ApiConfig
import ba.etf.rma26.projekat.data.models.Pitanje

object PitanjeKvizRepository {
    private val api get() = ApiConfig.service

    suspend fun getPitanja(idKviza: Int): List<Pitanje> = try {
        api.getPitanja(idKviza)
    } catch (e: Exception) {
        emptyList()
    }
}