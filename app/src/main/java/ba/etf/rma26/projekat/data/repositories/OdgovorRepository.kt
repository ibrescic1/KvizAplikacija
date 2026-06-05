package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.repositories.ApiConfig
import ba.etf.rma26.projekat.data.models.Odgovor
import ba.etf.rma26.projekat.data.models.OdgovorRequest

object OdgovorRepository {
    private val api get() = ApiConfig.service

    suspend fun getOdgovoriKviz(idKviza: Int): List<Odgovor> {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isBlank()) return emptyList()
            api.getOdgovoriKviz(hash, idKviza)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun postaviOdgovorKviz(idKvizTaken: Int, idPitanje: Int, odgovor: Int): Int {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isBlank()) return -1
            api.postaviOdgovor(hash, idKvizTaken, OdgovorRequest(idPitanje, odgovor))
        } catch (e: Exception) {
            -1
        }
    }
}