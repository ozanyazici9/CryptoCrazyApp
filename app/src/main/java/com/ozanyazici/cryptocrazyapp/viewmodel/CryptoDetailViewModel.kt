package com.ozanyazici.cryptocrazyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.ozanyazici.cryptocrazyapp.model.Crypto
import com.ozanyazici.cryptocrazyapp.repository.CryptoRepository
import com.ozanyazici.cryptocrazyapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoDetailViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    // Compose kullanırken viewmodel da suspend veya coroutinescope açmanın bedelleri var demiştik. Bu sefer suspend yaparak nasıl yapılacağını göreceğiz.

    suspend fun getCrypto() : Resource<Crypto> {
        return repository.getCrypto()
    }

}