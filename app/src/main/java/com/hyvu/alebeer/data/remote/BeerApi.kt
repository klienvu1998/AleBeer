package com.hyvu.alebeer.data.remote

import com.hyvu.alebeer.data.remote.entity.BeerResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {

    @GET("api/api-testing/sample-data")
    suspend fun getBeersInformation(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BeerResponse>


    companion object {
        fun create(): BeerApi {
            val client by lazy {
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor())
                    .build()
            }

            return  Retrofit.Builder()
                .baseUrl("https://apps.uthus.vn/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(BeerApi::class.java)
        }
    }
}