package ba.etfrma.spirala1.kvizaplikacija.viewmodel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
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

    init {
        ucitajKvizove()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun promijeniFilter(filter: FilterOpcija) {
        _selectedFilter.value = filter
        ucitajKvizove()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ucitajKvizove() {
        _kvizovi.value = when (_selectedFilter.value) {
            FilterOpcija.SVI_MOJI -> KvizStaticData.getUpisani()
            FilterOpcija.SVI      -> KvizStaticData.getAll()
            FilterOpcija.URADENI  -> KvizStaticData.getDone()
            FilterOpcija.BUDUCI   -> KvizStaticData.getFuture()
            FilterOpcija.PROSLI   -> KvizStaticData.getNotTaken()
        }
    }
}
