package ba.etf.rma26.projekat

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

/*
 glavno mjesto za sve konstante u aplikaciji, umjesto da su hardkodirane vrijednosti rasute po cijelom kodu,
 sve su ovdje, lakse se mijenjaju i lakse se pronalaze
 */
object Konstante {
    // referentni datum koji predstavlja trenutno za statusove kvizova
    @RequiresApi(Build.VERSION_CODES.O)
    val REFERENTNI_DATUM: LocalDateTime = LocalDateTime.of(2021, 6, 12, 12, 0)

    // default hash koji se koristi za demo korisnika
    const val DEFAULT_HASH = "demo"

    // Base URL za android emulator
    const val DEFAULT_BASE_URL = "http://10.0.2.2:3000/"
}
