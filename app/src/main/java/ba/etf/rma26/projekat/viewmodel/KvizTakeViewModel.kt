package ba.etf.rma26.projekat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.data.models.Pitanje
import ba.etf.rma26.projekat.data.repositories.OdgovorRepository
import ba.etf.rma26.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma26.projekat.data.repositories.TakeKvizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KvizTakeViewModel : ViewModel() {

    private val _pitanja = MutableStateFlow<List<Pitanje>>(emptyList())
    val pitanja: StateFlow<List<Pitanje>> = _pitanja

    private val _kvizTaken = MutableStateFlow<KvizTaken?>(null)
    val kvizTaken: StateFlow<KvizTaken?> = _kvizTaken

    private val _ukupniBodovi = MutableStateFlow<Int?>(null)
    val ukupniBodovi: StateFlow<Int?> = _ukupniBodovi

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _greska = MutableStateFlow<String?>(null)
    val greska: StateFlow<String?> = _greska

    fun zapocniKviz(idKviza: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _greska.value = null
            val taken = TakeKvizRepository.zapocniKviz(idKviza)
            if (taken == null) {
                _greska.value = "Nije moguće započeti kviz."
            } else {
                _kvizTaken.value = taken
                _pitanja.value = PitanjeKvizRepository.getPitanja(idKviza)
            }
            _isLoading.value = false
        }
    }

    fun posaljiOdgovor(idPitanje: Int, odgovor: Int) {
        val taken = _kvizTaken.value ?: return
        viewModelScope.launch {
            val bodovi = OdgovorRepository.postaviOdgovorKviz(taken.id, idPitanje, odgovor)
            if (bodovi >= 0) {
                _ukupniBodovi.value = bodovi
            } else {
                _greska.value = "Odgovor nije prihvaćen."
            }
        }
    }
}