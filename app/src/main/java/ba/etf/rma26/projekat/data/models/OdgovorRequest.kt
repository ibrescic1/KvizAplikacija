package ba.etf.rma26.projekat.data.models

import com.google.gson.annotations.SerializedName


data class OdgovorRequest(
    @SerializedName("idPitanje") val idPitanja: Int,
    val odgovor: Int
)
