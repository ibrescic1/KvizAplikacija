package ba.etfrma.spirala1.kvizaplikacija.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

data class Kviz(
    val naziv: String,
    val nazivPredmeta: String,
    val datumPocetak: LocalDateTime,
    val datumKraj: LocalDateTime,
    val datumRada: LocalDateTime?,
    val trajanje: Int,
    val nazivGrupe: String,
    val osvojeniBodovi: Float?
)
enum class KvizStatus {
    PLAVA,
    ZELENA,
    ZUTA,
    CRVENA
}

@RequiresApi(Build.VERSION_CODES.O)
fun Kviz.getStatus(referentniDatum: LocalDateTime): KvizStatus {
    if (datumRada != null) return KvizStatus.PLAVA
    return when {
        referentniDatum.isBefore(datumPocetak) -> KvizStatus.ZUTA
        referentniDatum.isAfter(datumKraj)     -> KvizStatus.CRVENA
        else                                    -> KvizStatus.ZELENA
    }
}