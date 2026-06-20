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

    // Prati indeks odabranog odgovora za svako pitanje
    private val _odabraniOdgovori = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val odabraniOdgovori: StateFlow<Map<Int, Int>> = _odabraniOdgovori

    // prati poruku ("Tačno" ili "Netačno") za svako pitanje
    private val _porukeOdgovora = MutableStateFlow<Map<Int, String>>(emptyMap())
    val porukeOdgovora: StateFlow<Map<Int, String>> = _porukeOdgovora

    fun zapocniKviz(idKviza: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _greska.value = null
            _odabraniOdgovori.value = emptyMap()
            _porukeOdgovora.value = emptyMap()

            val taken = TakeKvizRepository.zapocniKviz(idKviza)
            if (taken == null) {
                _greska.value = "Nije moguće započeti kviz."
            } else {
                _kvizTaken.value = taken
                _pitanja.value = PitanjeKvizRepository.getPitanja(idKviza)
                _ukupniBodovi.value = taken.osvojeniBodovi
            }
            _isLoading.value = false
        }
    }

    fun posaljiOdgovor(idPitanje: Int, odgovor: Int) {
        val taken = _kvizTaken.value ?: return

        // ignorise poziv ako je korisnik ponovo kliknuo na vec odabranu opciju
        if (_odabraniOdgovori.value[idPitanje] == odgovor) return

        viewModelScope.launch {
            // pamti bodove prije slanja novog odgovora kako bi znao da li su veci ili manji
            val prethodniBodovi = _ukupniBodovi.value ?: taken.osvojeniBodovi

            val bodovi = OdgovorRepository.postaviOdgovorKviz(taken.id, idPitanje, odgovor)
            if (bodovi >= 0) {
                // azuriram odabranu opciju na novu vrijednost
                _odabraniOdgovori.value = _odabraniOdgovori.value + (idPitanje to odgovor)

                // ako su novi bodovi veci od prethodnih, odgovor je tacan
                // ako su se smanjili ili ostali isti, odgovor je netacan
                val jeTacno = bodovi > prethodniBodovi
                val poruka = if (jeTacno) "Tačno!" else "Netačno!"
                _porukeOdgovora.value = _porukeOdgovora.value + (idPitanje to poruka)

                // azuriranje ukupnih bodova na trenutno stanje sa servera
                _ukupniBodovi.value = bodovi
            } else {
                _greska.value = "Odgovor nije prihvaćen."
            }
        }
    }
}