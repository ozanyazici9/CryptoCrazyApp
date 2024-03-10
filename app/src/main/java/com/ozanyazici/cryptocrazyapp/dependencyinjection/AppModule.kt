package com.ozanyazici.cryptocrazyapp.dependencyinjection

import com.ozanyazici.cryptocrazyapp.repository.CryptoRepository
import com.ozanyazici.cryptocrazyapp.service.CryptoAPI
import com.ozanyazici.cryptocrazyapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCryptoRepository(
        api: CryptoAPI
    ) =  CryptoRepository(api)

    @Singleton
    @Provides // hangi türden bir nesneyi döndüreceğini belirtir.
    fun provideCryptoApi(): CryptoAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(CryptoAPI::class.java)
    }

}