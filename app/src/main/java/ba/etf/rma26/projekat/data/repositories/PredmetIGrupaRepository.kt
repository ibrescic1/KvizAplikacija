package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.repositories.ApiConfig
import ba.etf.rma26.projekat.data.models.Grupa
import ba.etf.rma26.projekat.data.models.Predmet

object PredmetIGrupaRepository {
    private val api get() = ApiConfig.service

    suspend fun getPredmeti(): List<Predmet> = try {
        api.getPredmeti()
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun getGrupe(): List<Grupa> = try {
        api.getGrupe()
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun getGrupeZaPredmet(idPredmeta: Int): List<Grupa> = try {
        api.getGrupeZaPredmet(idPredmeta)
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun upisiUGrupu(idGrupa: Int): Boolean {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isBlank()) return false
            api.upisiUGrupu(hash, idGrupa)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUpisaneGrupe(): List<Grupa> {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isBlank()) return emptyList()
            api.getUpisaneGrupe(hash)
        } catch (e: Exception) {
            emptyList()
        }
    }
}