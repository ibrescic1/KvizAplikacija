package ba.etf.rma26.projekat.data.repositories

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val DEFAULT_URL = "http://10.0.2.2:3000/"
    private var baseUrl: String = DEFAULT_URL
    private var apiKey: String? = null

    fun postaviBaseURL(url: String) {
        baseUrl = if (url.endsWith("/")) url else "$url/"
        _retrofit = null
    }

    fun postaviApiKey(key: String?) {
        apiKey = key
        _retrofit = null
    }

    fun getApiKey(): String? = apiKey

    private var _retrofit: Retrofit? = null

    private fun buildRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val req = chain.request().newBuilder()
                apiKey?.let { req.addHeader("X-API-Key", it) }
                chain.proceed(req.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofit: Retrofit
        get() {
            if (_retrofit == null) _retrofit = buildRetrofit()
            return _retrofit!!
        }

    val service: RmaApiService
        get() = retrofit.create(RmaApiService::class.java)
}