package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.Grupa
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.data.models.Odgovor
import ba.etf.rma26.projekat.data.models.OdgovorRequest
import ba.etf.rma26.projekat.data.models.Pitanje
import ba.etf.rma26.projekat.data.models.Predmet
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RmaApiService {

    @GET("predmet")
    suspend fun getPredmeti(): List<Predmet>

    @GET("grupa")
    suspend fun getGrupe(): List<Grupa>

    @GET("predmet/{id}/grupa")
    suspend fun getGrupeZaPredmet(@Path("id") idPredmeta: Int): List<Grupa>

    @GET("student/{hash}/grupa")
    suspend fun getUpisaneGrupe(@Path("hash") hash: String): List<Grupa>

    @POST("student/{hash}/grupa/{id}")
    suspend fun upisiUGrupu(
        @Path("hash") hash: String,
        @Path("id") idGrupe: Int
    ): Boolean

    @GET("kviz")
    suspend fun getKvizovi(): List<Kviz>

    @GET("kviz/{id}")
    suspend fun getKvizById(@Path("id") id: Int): Kviz?

    @GET("student/{hash}/kviz")
    suspend fun getUpisaniKvizovi(@Path("hash") hash: String): List<Kviz>

    @GET("kviz/{id}/pitanja")
    suspend fun getPitanja(@Path("id") idKviza: Int): List<Pitanje>

    @POST("student/{hash}/kviz/{id}")
    suspend fun zapocniKviz(
        @Path("hash") hash: String,
        @Path("id") idKviza: Int
    ): KvizTaken

    @GET("student/{hash}/kviztaken")
    suspend fun getPocetiKvizovi(@Path("hash") hash: String): List<KvizTaken>?

    @GET("student/{hash}/kviz/{id}/odgovori")
    suspend fun getOdgovoriKviz(
        @Path("hash") hash: String,
        @Path("id") idKviza: Int
    ): List<Odgovor>

    @POST("student/{hash}/kviztaken/{id}/odgovor")
    suspend fun postaviOdgovor(
        @Path("hash") hash: String,
        @Path("id") idKvizTaken: Int,
        @Body body: OdgovorRequest
    ): Int
}