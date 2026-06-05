package ba.etf.rma26.projekat.data.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Kviz(
    val id: Int,
    val naziv: String,
    val idPredmeta: Int,
    val idGrupe: Int,
    val datumPocetka: String,
    val datumKraj: String,
    val trajanje: Int,
    val nazivPredmeta: String?,
    val nazivGrupe: String?
)

enum class KvizStatus {
    PLAVA, ZELENA, ZUTA, CRVENA
}

@RequiresApi(Build.VERSION_CODES.O)
fun Kviz.getStatus(referentniDatum: LocalDateTime): KvizStatus {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val pocetak = LocalDateTime.parse(datumPocetka, formatter)
    val kraj = LocalDateTime.parse(datumKraj, formatter)

    return when {
        referentniDatum.isBefore(pocetak) -> KvizStatus.ZUTA
        referentniDatum.isAfter(kraj)     -> KvizStatus.CRVENA
        else                              -> KvizStatus.ZELENA
    }
}