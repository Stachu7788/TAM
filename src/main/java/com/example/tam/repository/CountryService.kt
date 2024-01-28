package com.example.tam.repository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {
    @GET("/v3.1/all/")
    suspend fun getCountries(): Response<List<Country>>

    @GET("/v3.1/name/{code}")
    suspend fun getCountry(@Path("code") code: String): Response<List<Country>>

    companion object {
        private const val COUNTRY_URL = "https://restcountries.com/"

        private val logger = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        private val okHttp = OkHttpClient.Builder().apply {
            this.addInterceptor(logger)
        }.build()

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(COUNTRY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }

        val countryService: CountryService by lazy {
            retrofit.create(CountryService::class.java)
        }
    }
}