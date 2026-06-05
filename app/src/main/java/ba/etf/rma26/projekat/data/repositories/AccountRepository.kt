package ba.etf.rma26.projekat.data.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AccountRepository {
    private val _hash = MutableStateFlow("")
    val hashFlow: StateFlow<String> = _hash

    suspend fun postaviHash(acHash: String): Boolean {
        if (acHash.isBlank()) return false
        _hash.value = acHash
        return true
    }

    suspend fun getHash(): String = _hash.value
}