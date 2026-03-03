package org.delcom.pam_p4_ifs23019.network.desserts.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.delcom.pam_p4_ifs23019.BuildConfig
import java.util.concurrent.TimeUnit

class DessertAppContainer: IDessertAppContainer {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    val okHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }

        connectTimeout(2, TimeUnit.MINUTES)
        readTimeout(2, TimeUnit.MINUTES)
        writeTimeout(2, TimeUnit.MINUTES)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_PANTS_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val retrofitService: DessertApiService by lazy {
        retrofit.create(DessertApiService::class.java)
    }

    override val DessertRepository: IDessertRepository by lazy {
        DessertRepository(retrofitService)
    }

    override val dessertRepository: IDessertRepository
        get() = DessertRepository
}
