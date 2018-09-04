package com.mobitribe.app.ezzerecharge.network

import android.app.Activity
import android.provider.Settings
import android.util.Log
import base.ParentActivity
import com.example.sarwan.final_year_project.BuildConfig
import com.google.gson.GsonBuilder
import network.NetworkConstants
import network.WebServices
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RestClient {

    private val TIMEOUT = 25
    private var logging: HttpLoggingInterceptor? = null
    private val TAG: String = "RESTCLIENT"


    init {
        setupClient()
    }

    private fun setupClient() {

        setupLogging()
    }

    private fun setupLogging() {
        logging = HttpLoggingInterceptor()
        logging!!.level = if ( BuildConfig.DEBUG ) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }



    fun getRestAdapter(): WebServices {

        val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val newRequest: Request
                    newRequest = request.newBuilder()
                            .addHeader("secret", "8aj97ha919714ashfho01247012407nasni1lA97ba1")
                            .build()
                    chain.proceed(newRequest)
                }
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS).writeTimeout(TIMEOUT.toLong(),
                        TimeUnit.SECONDS).readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().
                baseUrl(NetworkConstants.BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                client(httpClient).
                build()

        return retrofit.create<WebServices>(WebServices::class.java!!)
    }
}
