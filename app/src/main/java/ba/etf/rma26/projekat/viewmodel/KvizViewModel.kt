package ba.etf.rma26.projekat.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.repositories.AccountRepository
import ba.etf.rma26.projekat.data.repositories.KvizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class FilterOpcija(val naziv: String) {
    SVI_MOJI("Svi moji kvizovi"),
    SVI("Svi kvizovi"),
    URADJENI("Urađeni kvizovi"),
    BUDUCI("Budući kvizovi"),
    PROSLI("Prošli kvizovi")
}

@RequiresApi(Build.VERSION_CODES.O)
class KvizViewModel : ViewModel() {

    private val referentniDatum = LocalDateTime.of(2026, 4, 10, 12, 0)

    private val _sviKvizovi = MutableStateFlow<List<Kviz>>(emptyList())
    private val _kvizovi = MutableStateFlow<List<Kviz>>(emptyList())
    val kvizovi: StateFlow<List<Kviz>> = _kvizovi

    private val _selectedFilter = MutableStateFlow(FilterOpcija.SVI_MOJI)
    val selectedFilter: StateFlow<FilterOpcija> = _selectedFilter

    private val _brojKvizova = MutableStateFlow(0)
    val brojKvizova: StateFlow<Int> = _brojKvizova

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {

        viewModelScope.launch {
            AccountRepository.hashFlow.collect { hash ->
                if (hash.isNotBlank()) {
                    osvjeziKvizove()
                }
            }
        }
    }

    fun osvjeziKvizove() {
        viewModelScope.launch {
            _isLoading.value = true
            _sviKvizovi.value = KvizRepository.getUpisani()
            primijeniFilter()
            _isLoading.value = false
        }
    }

    fun promijeniFilter(opcija: FilterOpcija) {
        _selectedFilter.value = opcija
        primijeniFilter()
    }

    private fun primijeniFilter() {
        val sve = _sviKvizovi.value
        val filtered = when (_selectedFilter.value) {
            FilterOpcija.SVI_MOJI -> sve
            FilterOpcija.SVI      -> sve
            FilterOpcija.URADJENI -> sve.filter { kvizJeProsli(it) }
            FilterOpcija.BUDUCI   -> sve.filter { kvizJeBuduci(it) }
            FilterOpcija.PROSLI   -> sve.filter { kvizJeProsli(it) }
        }
        _kvizovi.value = filtered.sortedBy { it.datumPocetka }
        _brojKvizova.value = filtered.size
    }

    private fun parseDate(iso: String): LocalDateTime =
        LocalDateTime.parse(iso, DateTimeFormatter.ISO_DATE_TIME)

    private fun kvizJeBuduci(kviz: Kviz): Boolean =
        parseDate(kviz.datumPocetka).isAfter(referentniDatum)

    private fun kvizJeProsli(kviz: Kviz): Boolean =
        parseDate(kviz.datumKraj).isBefore(referentniDatum)
}