package ba.etf.rma26.projekat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.etf.rma26.projekat.data.models.Grupa
import ba.etf.rma26.projekat.data.models.Predmet
import ba.etf.rma26.projekat.data.repositories.PredmetIGrupaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpisViewModel : ViewModel() {

    private val _dostupniPredmeti = MutableStateFlow<List<Predmet>>(emptyList())
    val dostupniPredmeti: StateFlow<List<Predmet>> = _dostupniPredmeti

    private val _dostupneGrupe = MutableStateFlow<List<Grupa>>(emptyList())
    val dostupneGrupe: StateFlow<List<Grupa>> = _dostupneGrupe

    private val _odabranaGodina = MutableStateFlow<Int?>(null)
    val odabranaGodina: StateFlow<Int?> = _odabranaGodina

    private val _odabranPredmet = MutableStateFlow<Predmet?>(null)
    val odabranPredmet: StateFlow<Predmet?> = _odabranPredmet

    private val _odabranaGrupa = MutableStateFlow<Grupa?>(null)
    val odabranaGrupa: StateFlow<Grupa?> = _odabranaGrupa

    private val _mozeDaSeUpise = MutableStateFlow(false)
    val mozeDaSeUpise: StateFlow<Boolean> = _mozeDaSeUpise

    private val _upisSuccess = MutableStateFlow<Boolean?>(null)
    val upisSuccess: StateFlow<Boolean?> = _upisSuccess

    fun odaberiGodinu(godina: Int) {
        _odabranaGodina.value = godina
        _odabranPredmet.value = null
        _odabranaGrupa.value = null
        _mozeDaSeUpise.value = false
        viewModelScope.launch {
            val predmeti = PredmetIGrupaRepository.getPredmeti()
            _dostupniPredmeti.value = predmeti.filter { it.godina == godina }
        }
    }

    fun odaberiPredmet(predmet: Predmet) {
        _odabranPredmet.value = predmet
        _odabranaGrupa.value = null
        _mozeDaSeUpise.value = false
        viewModelScope.launch {
            _dostupneGrupe.value = PredmetIGrupaRepository.getGrupeZaPredmet(predmet.id)
        }
    }

    fun odaberiGrupu(grupa: Grupa) {
        _odabranaGrupa.value = grupa
        _mozeDaSeUpise.value = true
    }

    fun upisi(onUpisZavrsen: () -> Unit = {}) {
        val idGrupe = _odabranaGrupa.value?.id ?: return
        viewModelScope.launch {
            val result = PredmetIGrupaRepository.upisiUGrupu(idGrupe)
            _upisSuccess.value = result
            if (result) {
                _mozeDaSeUpise.value = false
                _odabranaGodina.value = null
                _odabranPredmet.value = null
                _odabranaGrupa.value = null
                _dostupniPredmeti.value = emptyList()
                _dostupneGrupe.value = emptyList()
                onUpisZavrsen()
            }
        }
    }
}