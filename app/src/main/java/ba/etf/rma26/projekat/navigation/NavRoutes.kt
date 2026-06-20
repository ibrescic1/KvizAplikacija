package ba.etf.rma26.projekat.navigation

object NavRoutes {
    const val FILTER_SCREEN = "filter_screen"
    const val KVIZOVI_SCREEN = "kvizovi_screen"
    const val KVIZ_DETALJI_SCREEN = "kviz_detalji/{idKviza}"
    const val KVIZ_TAKE_SCREEN = "kviz_take/{idKviza}"

    fun kvizDetaljiRoute(idKviza: Int) = "kviz_detalji/$idKviza"
    fun kvizTakeRoute(idKviza: Int) = "kviz_take/$idKviza"

}

