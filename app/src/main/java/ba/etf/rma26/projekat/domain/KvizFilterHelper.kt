package ba.etf.rma26.projekat.domain

import android.os.Build
import androidx.annotation.RequiresApi
import ba.etf.rma26.projekat.Konstante
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.viewmodel.FilterOpcija
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object KvizFilterHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    private val isoFormatter = DateTimeFormatter.ISO_DATE_TIME


     // parsira ISO datum string u LocalDateTime, moze i datume s timezone oznakom Z (npr. "2021-05-10T08:00:00.000Z") koje backend vraća, a LocalDateTime.parse sam ne može parsirati.

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDate(iso: String): LocalDateTime {
        return try {
            // Konvertuje datum
            ZonedDateTime.parse(iso)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime()
        } catch (e: Exception) {
            try {
                LocalDateTime.parse(iso, isoFormatter)
            } catch (e2: Exception) {
                LocalDateTime.MIN
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun kvizJeBuduci(kviz: Kviz): Boolean =
        parseDate(kviz.datumPocetka).isAfter(Konstante.REFERENTNI_DATUM)

    @RequiresApi(Build.VERSION_CODES.O)
    fun kvizJeProsli(kviz: Kviz): Boolean =
        parseDate(kviz.datumKraj).isBefore(Konstante.REFERENTNI_DATUM)

    @RequiresApi(Build.VERSION_CODES.O)
    fun kvizJeAktivan(kviz: Kviz): Boolean {
        val pocetak = parseDate(kviz.datumPocetka)
        val kraj = parseDate(kviz.datumKraj)
        return Konstante.REFERENTNI_DATUM.isAfter(pocetak) &&
                Konstante.REFERENTNI_DATUM.isBefore(kraj)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filtriraj(kvizovi: List<Kviz>, opcija: FilterOpcija): List<Kviz> {
        val filtered = when (opcija) {
            FilterOpcija.SVI_MOJI -> kvizovi
            FilterOpcija.SVI      -> kvizovi
            FilterOpcija.URADJENI -> kvizovi.filter { kvizJeProsli(it) }
            FilterOpcija.BUDUCI   -> kvizovi.filter { kvizJeBuduci(it) }
            FilterOpcija.PROSLI   -> kvizovi.filter { kvizJeProsli(it) }
        }
        return filtered.sortedBy { it.datumPocetka }
    }

    fun izracunajProsjek(zapoceti: List<KvizTaken>?): Float? {
        val saOdgovorima = zapoceti?.filter { it.osvojeniBodovi > 0 }
        return if (!saOdgovorima.isNullOrEmpty()) {
            saOdgovorima.map { it.osvojeniBodovi }.average().toFloat()
        } else null
    }
}