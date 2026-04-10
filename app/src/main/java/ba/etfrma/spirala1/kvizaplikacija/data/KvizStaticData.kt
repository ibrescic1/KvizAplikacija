package ba.etfrma.spirala1.kvizaplikacija.data

import android.os.Build
import androidx.annotation.RequiresApi
import ba.etfrma.spirala1.kvizaplikacija.model.Kviz
import java.time.LocalDateTime
@RequiresApi(Build.VERSION_CODES.O)
val referentniDatum = LocalDateTime.of(2026, 4, 10, 12, 0)
object KvizStaticData {
    @RequiresApi(Build.VERSION_CODES.O)
    private val sviKvizovi = listOf(

        //plava-urađen
        Kviz(
            naziv = "Kviz 1",
            nazivPredmeta = "RMA",
            datumPocetak = LocalDateTime.of(2026, 4, 2, 10, 0),
            datumKraj = LocalDateTime.of(2026, 4, 2, 10, 30),
            datumRada = LocalDateTime.of(2026, 4, 2, 10, 15),
            trajanje = 30,
            nazivGrupe = "1a",
            osvojeniBodovi = 8f
        ),

        //crvena-prošao, nije urađen
        Kviz(
            naziv = "Kviz 2",
            nazivPredmeta = "RMA",
            datumPocetak = LocalDateTime.of(2026, 4, 3, 11, 0),
            datumKraj = LocalDateTime.of(2026, 4, 3, 11, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1b",
            osvojeniBodovi = null
        ),

        //zelena-aktivan, nije urađen
        Kviz(
            naziv = "Kviz 3",
            nazivPredmeta = "RMA",
            datumPocetak = LocalDateTime.of(2026, 4, 9, 11, 45),
            datumKraj = LocalDateTime.of(2026, 4, 9, 12, 30),
            datumRada = null,
            trajanje = 45,
            nazivGrupe = "1a",
            osvojeniBodovi = null
        ),

        //zuta-tek treba biti aktivan
        Kviz(
            naziv = "Kviz 4",
            nazivPredmeta = "RMA",
            datumPocetak = LocalDateTime.of(2026, 4, 12, 10, 0),
            datumKraj = LocalDateTime.of(2026, 4, 12, 10, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1b",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 1",
            nazivPredmeta = "UUP",
            datumPocetak = LocalDateTime.of(2026, 4, 5, 9, 0),
            datumKraj = LocalDateTime.of(2026, 4, 5, 9, 30),
            datumRada = LocalDateTime.of(2026, 4, 5, 9, 20),
            trajanje = 30,
            nazivGrupe = "1a",
            osvojeniBodovi = 9f
        ),

        Kviz(
            naziv = "Kviz 2",
            nazivPredmeta = "UUP",
            datumPocetak = LocalDateTime.of(2026, 4, 14, 13, 0),
            datumKraj = LocalDateTime.of(2026, 4, 14, 13, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1b",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 3",
            nazivPredmeta = "UUP",
            datumPocetak = LocalDateTime.of(2026, 4, 16, 14, 0),
            datumKraj = LocalDateTime.of(2026, 4, 16, 14, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1a",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 4",
            nazivPredmeta = "UUP",
            datumPocetak = LocalDateTime.of(2026, 4, 18, 15, 0),
            datumKraj = LocalDateTime.of(2026, 4, 18, 15, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1b",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 1",
            nazivPredmeta = "DM",
            datumPocetak = LocalDateTime.of(2026, 4, 15, 8, 0),
            datumKraj = LocalDateTime.of(2026, 4, 15, 8, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1a",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 2",
            nazivPredmeta = "DM",
            datumPocetak = LocalDateTime.of(2026, 4, 16, 8, 0),
            datumKraj = LocalDateTime.of(2026, 4, 16, 8, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1b",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 1",
            nazivPredmeta = "RPR",
            datumPocetak = LocalDateTime.of(2026, 4, 17, 9, 0),
            datumKraj = LocalDateTime.of(2026, 4, 17, 9, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1a",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 2",
            nazivPredmeta = "RPR",
            datumPocetak = LocalDateTime.of(2026, 4, 18, 9, 0),
            datumKraj = LocalDateTime.of(2026, 4, 18, 9, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1b",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 1",
            nazivPredmeta = "OIS",
            datumPocetak = LocalDateTime.of(2026, 4, 19, 10, 0),
            datumKraj = LocalDateTime.of(2026, 4, 19, 10, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1a",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 2",
            nazivPredmeta = "OIS",
            datumPocetak = LocalDateTime.of(2026, 4, 20, 10, 0),
            datumKraj = LocalDateTime.of(2026, 4, 20, 10, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1b",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 1",
            nazivPredmeta = "TP",
            datumPocetak = LocalDateTime.of(2026, 4, 21, 11, 0),
            datumKraj = LocalDateTime.of(2026, 4, 21, 11, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1a",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "Kviz 2",
            nazivPredmeta = "TP",
            datumPocetak = LocalDateTime.of(2026, 4, 22, 11, 0),
            datumKraj = LocalDateTime.of(2026, 4, 22, 11, 30),
            datumRada = null,
            trajanje = 30,
            nazivGrupe = "1b",
            osvojeniBodovi = null
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReferentDate(): LocalDateTime = LocalDateTime.of(2021, 4, 11, 10, 0)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(): List<Kviz> = sviKvizovi.sortedBy { it.datumPocetak }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUpisani(): List<Kviz> = sviKvizovi
        .filter { it.nazivPredmeta in PredmetStaticData.getUpisani().map { p -> p.naziv } }
        .sortedBy { it.datumPocetak }

            @RequiresApi(Build.VERSION_CODES.O)
    fun getDone() = getUpisani().filter { it.datumRada != null }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFuture() = getUpisani().filter { it.datumPocetak.isAfter(getReferentDate()) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotTaken() = getUpisani().filter { it.datumKraj.isBefore(getReferentDate()) && it.datumRada == null }
}