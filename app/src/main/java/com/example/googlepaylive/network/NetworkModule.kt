package com.example.googlepaylive.network

import com.example.googlepaylive.models.CreatePaymentSessionsResponseJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
internal object NetworkModule {
    @Provides
    @ViewModelScoped
    internal fun moshi(): Moshi =
        Moshi.Builder()
            .add(CreatePaymentSessionsResponseJsonAdapter.Factory)
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @ViewModelScoped
    internal fun httpClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY },
        ).build()

    @Provides
    @ViewModelScoped
    internal fun paymentSessionClient(
        moshi: Moshi,
        httpClient: OkHttpClient,
    ): PaymentSessionsApi =
        Retrofit
            .Builder()
            .baseUrl("https://api.sandbox.checkout.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .build()
            .create(PaymentSessionsApi::class.java)
}
