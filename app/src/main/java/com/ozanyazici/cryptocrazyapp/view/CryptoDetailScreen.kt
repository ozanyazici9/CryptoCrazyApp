package com.ozanyazici.cryptocrazyapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ozanyazici.cryptocrazyapp.model.Crypto
import com.ozanyazici.cryptocrazyapp.util.Resource
import com.ozanyazici.cryptocrazyapp.viewmodel.CryptoDetailViewModel
import kotlinx.coroutines.launch


@Composable
fun CryptoDetailScreen(
    id: String,
    price: String,
    navController: NavController,
    viewModel: CryptoDetailViewModel = hiltViewModel()
) {

    // DetailViewModel da getCrypto metodunu suspend olarak belirttiğimiz(orda belirttiğim bedel bu) için coroutine scope u burada açmalıyız.
    // Fakat coroutineScope u kullandığımız yer çok önemli. Şimdi yapılmaması gereken senaryoyu görmek için yapacağız.

    // Birici adım yanlış çünkü burada cryptoScope içinde yaptığımız viewModel.getCrypto işlemi görünümde değişikliğe yol açıyor
    // bu sebeple CryptoDetailScreen metodu baştan çalıştırılıyor.
    // Ve coroutinescope yeniden çalıştırılıyor ve bu devamlı devam ettiği için sürekli istek atılıyor.
    // Buna side - effect deniyor yeni uygulamanın stateinde değişiklik yapan ve composable ın dışında olan durumlara deniyor.
    // Bu nedenle bu yöntem verimsiz.
    // Side-effects in Compose dökümantasyonuna bak.

    /*
    // Step 1 -> Wrong
    val scope = rememberCoroutineScope()

    //Resource.Loading i başlangıç değeri olarak verdik.
    var cryptoItem by remember { mutableStateOf<Resource<Crypto>>(Resource.Loading()) }

    scope.launch {
        cryptoItem = viewModel.getCrypto()
        println(cryptoItem.data)
    }
     */

    /*
    //Step 2 -> Better
    var cryptoItem by remember { mutableStateOf<Resource<Crypto>>(Resource.Loading()) }

//key parametresi ne zaman çalıştırılacağını belirtiyor.
    LaunchedEffect(key1 = Unit) {
        cryptoItem = viewModel.getCrypto()
        println(cryptoItem.data)
    }
     */

    //Step 3 -> Verimliliği Step 2 ile aynı fakat yazımı daha kısa.
    val cryptoItem = produceState<Resource<Crypto>>(initialValue = Resource.Loading()){
        value = viewModel.getCrypto()
    }.value

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            when(cryptoItem) {

                is Resource.Success -> {
                    //CryptoItem tek elemanlı bir liste .name i direkt almak için elemanı içinden aldım.
                    val selectedCrypto = cryptoItem.data!![0]
                    Text(text = selectedCrypto.name,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(2.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Image(
                        painter = rememberImagePainter(data = selectedCrypto.logo_url),
                        contentDescription = selectedCrypto.name,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(200.dp, 200.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )

                    Text(text = price,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(2.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Center
                    )
                }

                is Resource.Error -> {
                    Text(text = cryptoItem.message!!)
                }

                is Resource.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}