package com.ozanyazici.cryptocrazyapp.service

import com.ozanyazici.cryptocrazyapp.model.Crypto
import com.ozanyazici.cryptocrazyapp.model.CryptoList
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoAPI {

    //atilsamancioglu/IA32-CryptoComposeData/main/cryptolist.json

    @GET("atilsamancioglu/IA32-CryptoComposeData/main/cryptolist.json")
    suspend fun getCryptoList() : CryptoList

    // Tıklanılan her crypto da aynı bilgileri göstereceğiz çünkü çalışan bir API kullanmıyoruz.

    //atilsamancioglu/IA32-CryptoComposeData/main/crypto.json

    @GET("atilsamancioglu/IA32-CryptoComposeData/main/crypto.json")
    suspend fun getCrypto() : Crypto

    /*

    Eğer çalışan bir APİ ye istek atsaydım burası böyle olacaktı.

    //prices?key=********************************

    @GET("prices")
    suspend fun getCryptoList(
        @Query("key") key: String
    ) : Unit

    //currencies?key=****************************************=BTC&attributes=id,name,logo_url

    Tıklanılan crypto ya göre veriler getirileceği için url yi bu metod çağrıldığında girilecek parametrelerle oluşturacağız.

    @GET("currencies")
    suspend fun getCrypto(
        @Query("key") key: String,
        @Query("ids") id: String,
        @Query("attributes") attributes: String
    ) : Unit

     */




}