package com.example.billbill_template.Login.signup

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private const val BASE_URL = "http://ec2-54-180-208-31.ap-northeast-2.compute.amazonaws.com:3000/"

    // Context가 필요하므로 초기화 시 제공해주는 방식으로 수정
    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    // 토큰을 자동으로 추가하는 인터셉터
    private val authInterceptor = Interceptor { chain ->
        if (!::appContext.isInitialized) {
            throw UninitializedPropertyAccessException("RetrofitClient has not been initialized. Please call initialize(context) before using the client.")
        }
        val requestBuilder = chain.request().newBuilder()

        // SharedPreferences에서 토큰을 가져와 헤더에 추가
        val sharedPreferences = appContext.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor) // 토큰을 추가하는 인터셉터
        .addInterceptor(loggingInterceptor) // 로깅 인터셉터
        .build()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
