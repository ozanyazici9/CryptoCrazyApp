package com.ozanyazici.cryptocrazyapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ozanyazici.cryptocrazyapp.model.Crypto
import com.ozanyazici.cryptocrazyapp.model.CryptoListItem
import com.ozanyazici.cryptocrazyapp.viewmodel.CryptoListViewModel

@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {

            Text(text = "Crypto Crazy", modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                textAlign = TextAlign.Center,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))
            //Search
            SearchBar(
                hint = "Search....",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Burası onSearch parametresi lambda bir metod burada girilen texti viewmodel a göndereceğim.
                viewModel.searchCryptoList(it)
            }
            Spacer(modifier = Modifier.height(10.dp))
            //List
            CryptoList(navController = navController)
        }
    }
}

// Searchbar biraz uzun olabileceği için farklı bir composable olarak yazıyoruz. Böyle oluştşrulan bileşenler farklı yerlerde de kullanılabilir.
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    // SearchBar a bir şey yazıldığında bana bir kod bloğu açıcak ve yapılan aramayı String olarak vericek bana ve viewmodeldan arama metodunu çağırabileceğim.
    onSearch: (String) -> Unit = {}
) {

    // hint parametresine değer verilmesse isHintDisplayed false olacak ve gösterilmeyecek.
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    // by, beni text.value yazarak string almaktan kurtarıyor ve textfield da direkt text yazabiliyorum.
    var text by remember {
        mutableStateOf("")
    }

    // Box, içine yerleştirilen bileşenleri üst üste veya yan yana düzenlemek için kullanılabilir
    // ve genellikle karmaşık düzenler oluşturmak için temel bir yapı taşı olarak kullanılır. Burada textfield ile hint in gösterildiği text i üst üste gösteriyor.
    Box(modifier = modifier) {

        // BasicTextField ın textField dan farkı daha temel olması ve daha fazla özelliğinin olması burada TextField da kullanabilirdik.
        BasicTextField(value = text, onValueChange = {
            text = it
            onSearch(it)
        },  maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                //onFocusChanged bileşenin o an kullanılıp kullanılmadığını temsil eder. Dolayısıyla, odak durumuna dayalı olarak farklı işlevlerin çalıştırılmasını sağlar.
                .onFocusChanged {
                    // isHintDisplayed değişkeni, metin alanının odaklanmadığı ve içeriğinin boş olduğu durumlarda true değerini alır.
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                })

        if (isHintDisplayed) {
            Text(text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp,vertical = 12.dp)
            )
        }
    }
}

// Burada viewmodeldaki mutableDataları alıyorum. CryptoList i  ListView a ordan da CryptoRow a gönderiyorum.
@Composable
fun CryptoList(navController: NavController,
               viewModel: CryptoListViewModel = hiltViewModel()
) {
    val cryptoList by remember {viewModel.cryptoList}
    val errorMessage by remember {viewModel.errorMessage}
    val isLoading by remember {viewModel.isLoading}

    CryptoListView(cryptos = cryptoList, navController = navController)

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if(isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        if(errorMessage.isNotEmpty()) {
            // retryView
            // Eğer hata verirse, butona tıklandığında cryptolar yeniden yüklenecek.
            RetryView(error = errorMessage) {
                viewModel.loadCryptos()
            }
        }
    }
}

// Row u kullanacağımız ListView
@Composable
fun CryptoListView(cryptos: List<CryptoListItem>,navController: NavController) {
    // LazyColumn RecyclerView ın Composedaki karşılığı
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(cryptos) { crypto ->
            CryptoRow(navController = navController, crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(navController: NavController, crypto: CryptoListItem) {
    Column( modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.surface)
        .clickable {
            navController.navigate(
                "crypto_detail_screen/${crypto.currency}/${crypto.price}"
            )
        }
    ) {

        Text(text = crypto.currency,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(text = crypto.price,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(2.dp),
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
// Hata alınırsa hatanın gösterileceği Composable
fun RetryView(
    error: String,
    onRetry: () -> Unit
) {
    Column() {
        Text(error, color = Color.Red, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            onRetry
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}






















