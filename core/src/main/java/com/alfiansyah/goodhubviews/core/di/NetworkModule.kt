package com.alfiansyah.goodhubviews.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import com.alfiansyah.goodhubviews.core.BuildConfig
import com.alfiansyah.goodhubviews.core.data.source.remote.network.ApiService
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient{
        val builder = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG){
            // Create the Collector
            val chuckerCollector = ChuckerCollector(
                context = context,
                // Toggles visibility of the notification
                showNotification = true,
                // Allows to customize the retention period of collected data
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            )
            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .collector(chuckerCollector)
                .maxContentLength(250_000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(true)
                .build()
            builder.addInterceptor(chuckerInterceptor)
        }
        return builder.build()

    }

    @Provides
    fun provideApiService(client: OkHttpClient): ApiService{
         val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

}