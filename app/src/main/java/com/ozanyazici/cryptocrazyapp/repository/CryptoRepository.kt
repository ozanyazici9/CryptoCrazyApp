package com.ozanyazici.cryptocrazyapp.repository

import com.ozanyazici.cryptocrazyapp.model.Crypto
import com.ozanyazici.cryptocrazyapp.model.CryptoList
import com.ozanyazici.cryptocrazyapp.service.CryptoAPI
import com.ozanyazici.cryptocrazyapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

// @ActivityScoped: Bu anotasyon, bir sınıfın, bir Android aktivitesiyle ilişkilendirilmiş bir kapsamda (scope) olduğunu belirtir.
// Aktivite kapsamı, bir aktivitenin yaşam döngüsüyle aynı veya daha uzun bir süre boyunca varlığını sürdürür.
// Yani, bu sınıfın örneği, aktivite var olduğu sürece yaşar ve aktivite sonlandığında yok olur.
// Bu anotasyon, aynı aktivite yaşam döngüsü boyunca farklı bileşenler arasında aynı örneğin paylaşılmasını sağlar.
@ActivityScoped
class CryptoRepository @Inject constructor(
    private val api: CryptoAPI
) {
    suspend fun getCryptoList(): Resource<CryptoList> {
        val response = try {
            api.getCryptoList()
        } catch (e: Exception) {
            return Resource.Error("Error.")
        }
        return Resource.Success(response)
    }

    suspend fun getCrypto(): Resource<Crypto> {
        val response = try {
            api.getCrypto()
        } catch (e: Exception) {
            return Resource.Error("Error.")
        }
        return Resource.Success(response)
    }
}