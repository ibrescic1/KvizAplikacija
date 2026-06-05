package ba.etf.rma26.projekat.navigation

object NavRoutes {
    const val FILTER_SCREEN = "filter_screen"
    const val KVIZOVI_SCREEN = "kvizovi_screen"

    const val KVIZ_TAKE_SCREEN = "kviz_take/{idKviza}"

    fun kvizTakeRoute(idKviza: Int) = "kviz_take/$idKviza"
}
