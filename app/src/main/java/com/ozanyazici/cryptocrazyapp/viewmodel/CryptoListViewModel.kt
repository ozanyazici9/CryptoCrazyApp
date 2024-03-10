package com.ozanyazici.cryptocrazyapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozanyazici.cryptocrazyapp.model.CryptoListItem
import com.ozanyazici.cryptocrazyapp.repository.CryptoRepository
import com.ozanyazici.cryptocrazyapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    // Compose kullanırken liveData yerine Mutable değişkenler kullanabilirim. LiveData da kullanabilirim istersem.
    var cryptoList = mutableStateOf<List<CryptoListItem>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    // Ben arama yaptıkça kalan sonuçlar bana gösterilecek bunun olamsı için cryptoList devamlı değişicek.
    // Diyelimki kullanıcı yazarken vazgeçti sildi asıl listeyi tekrar indirmemek için önce initialCryptoList içine kaydedeceğim.
    private var initialCryptoList = listOf<CryptoListItem>()
    private var isSearchStarting = true

    // Kotlin'de init bloğu, bir sınıfın başlatılması sırasında çalıştırılacak olan bir bloktur.
    // Bir sınıfın örneği oluşturulduğunda, init bloğu, sınıfın ana yapısını oluşturmak için kullanılabilir
    // ve özellikle örneğin ilk durumunu ayarlamak için yararlıdır.
    init {
        loadCryptos()
    }

    // ilk arama yapılmadan önce isSearchStarting true bu yüzden ilk aramada cryptoList i kullanıyoruz.
    // Daha sonra o listeden filtreleme işlemi yapılıyor ve isSearchStarting true olduğu için cryptoList i initialCryptoList e kaydediyoruz
    // ve isSearchStarting i false olarak ayarlıyoruz çünkü arama sonuçlarını cryptoList e atadığımızda tekrar arama yapılırsa bütün listede arama yapılması için
    // initial listi kullanmalıyız. Arama işlemi sırasında kullanıcı text i silerse kullanıcı arayüzünde cryptoList gösterildiği için
    // initialList e yedeklediğim veriyi cryptoList e atıyorum böylelikle uı da yine bütün liste gözüküyor.
    fun searchCryptoList(query: String) {
        val listToSearch = if (isSearchStarting) {
            cryptoList.value
        } else {
            initialCryptoList
        }

        // Arama işlemini coroutine ile default threadinde yapıyorum çünkü arama işlemi cpu yoğun bir işlem.
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                cryptoList.value = initialCryptoList
                isSearchStarting = true
                return@launch // Arama yapılacak liste değiştiği için coroutine in yeni şartlarda yeniden başlamasını sağlar.
            }

            val results = listToSearch.filter {
                it.currency.contains(query.trim(),ignoreCase = true)
            }

            if (isSearchStarting) {
                initialCryptoList = cryptoList.value
                isSearchStarting = false
            }

            cryptoList.value = results
        }
    }

    // Burada getCryptoList metodunu kullanmamız için ya bu metoduda suspend yapıcağız yada metodu coroutine içinde çağıracağız .
    // Compose da bu iki yönteminde bedelleri var o yüzden 2 yöntemide bu projede deneyeceğiz.
    fun loadCryptos() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getCryptoList()
            when(result) {
                is Resource.Success -> {
                    // burada mapIndexed metodu result.data nın içindeki listenin herbir elemanı
                    // CryptoListItem a dönüstürüp cryptoItems ın içine atıyor.
                    // Aslında result.data zaten bize İçinde CryptoListItem lar olan bir liste döndürüyor
                    // o yüzden direkt cryptoList içine result.data atanabilir ama burada mapIndexed ı da görmek için böyle tutucam.
                    val cryptoItems = result.data!!.mapIndexed { index, item ->
                        CryptoListItem(item.currency,item.price)
                    } as List<CryptoListItem>

                    errorMessage.value = ""
                    isLoading.value = false
                    cryptoList.value += cryptoItems
                }

                is Resource.Error -> {
                    isLoading.value = false
                    errorMessage.value = result.message!!
                }

                is Resource.Loading -> {}
                else -> {}
            }
        }
    }
}