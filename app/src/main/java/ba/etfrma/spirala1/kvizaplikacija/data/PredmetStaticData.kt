package ba.etfrma.spirala1.kvizaplikacija.data

import ba.etfrma.spirala1.kvizaplikacija.model.Predmet
import ba.etfrma.spirala1.kvizaplikacija.model.Grupa

object PredmetStaticData {

    private val sviPredmeti = listOf(
        Predmet("RMA", 2),
        Predmet("TP", 1),
        Predmet("UUP", 1),
        Predmet("DM", 2),
        Predmet("OIS", 3)
    )

    private val upisani = mutableListOf("RMA", "TP")

    fun getAll(): List<Predmet> {
        return sviPredmeti
    }

    fun getUpisani(): List<Predmet> = sviPredmeti.filter { it.naziv in upisani }

    fun getNeupisani(): List<Predmet> = sviPredmeti.filter { it.naziv !in upisani }
    fun upisiNaPredmet(predmet: Predmet) {
        if (predmet.naziv !in upisani) {
            upisani.add(predmet.naziv)
        }
    }
}
