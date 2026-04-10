package ba.etfrma.spirala1.kvizaplikacija.data

import ba.etfrma.spirala1.kvizaplikacija.model.Grupa

object GrupaStaticData {

    private val grupe = listOf(
        Grupa("1a", "VIS"),
        Grupa("1b", "VIS"),
        Grupa("1a", "RPR"),
        Grupa("1b", "RPR"),
        Grupa("1a", "DM"),
        Grupa("1b", "DM"),
        Grupa("1a", "RMA"),
        Grupa("1b", "RMA"),
        Grupa("1a", "UUP"),
        Grupa("1b", "UUP"),
        Grupa("1a", "TP"),
        Grupa("1b", "TP"),

        )

    fun getGrupaFromPredmet(nazivPredmeta: String): List<Grupa> =
        grupe.filter { it.nazivPredmeta == nazivPredmeta }
}
