package ba.etfrma.spirala1.kvizaplikacija.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import ba.etfrma.spirala1.kvizaplikacija.data.KvizStaticData
import ba.etfrma.spirala1.kvizaplikacija.model.Kviz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@RequiresApi(Build.VERSION_CODES.O)
enum class FilterOpcija(val naziv: String) {
    SVI_MOJI("Svi moji kvizovi"),
    SVI("Svi kvizovi"),
    URADENI("Urađeni kvizovi"),
    BUDUCI("Budući kvizovi"),
    PROSLI("Prošli kvizovi")
}

@RequiresApi(Build.VERSION_CODES.O)
class KvizViewModel : ViewModel() {

    private val _selectedFilter = MutableStateFlow(FilterOpcija.SVI_MOJI)
    val selectedFilter: StateFlow<FilterOpcija> = _selectedFilter.asStateFlow()

    private val _kvizovi = MutableStateFlow<List<Kviz>>(emptyList())
    val kvizovi: StateFlow<List<Kviz>> = _kvizovi.asStateFlow()

    // Broj kvizova za trenutni filter — prikazuje se na FilterScreen
    private val _brojKvizova = MutableStateFlow(0)
    val brojKvizova: StateFlow<Int> = _brojKvizova.asStateFlow()

    init {
        ucitajKvizove()
    }

    fun promijeniFilter(filter: FilterOpcija) {
        _selectedFilter.value = filter
        ucitajKvizove()
    }

    /**
     * Poziva se nakon upisa na predmet da bi se lista i broj osvježili
     * bez mijenjanja odabranog filtera.
     */
    fun osvjeziKvizove() {
        ucitajKvizove()
    }

    private fun ucitajKvizove() {
        val lista = when (_selectedFilter.value) {
            FilterOpcija.SVI_MOJI -> KvizStaticData.getUpisani()
            FilterOpcija.SVI      -> KvizStaticData.getAll()
            FilterOpcija.URADENI  -> KvizStaticData.getDone()
            FilterOpcija.BUDUCI   -> KvizStaticData.getFuture()
            FilterOpcija.PROSLI   -> KvizStaticData.getNotTaken()
        }
        _kvizovi.value = lista
        _brojKvizova.value = lista.size
    }
}