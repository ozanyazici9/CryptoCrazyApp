package com.ozanyazici.cryptocrazyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ozanyazici.cryptocrazyapp.ui.theme.CryptoCrazyAppTheme
import com.ozanyazici.cryptocrazyapp.view.CryptoDetailScreen
import com.ozanyazici.cryptocrazyapp.view.CryptoListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoCrazyAppTheme {
               // Composabalelardan  birinde değişkeninde vs. bir değişiklik olursa bütün ekran başta oluşturuluyor.
               // Bu oluşturulma sırasında kullandığımız ve değişmeyecek olan değişkenleri
               // remember olarak ayarlarız değeri aynı kalır tekrar initialize edilmesine gerek kalmaz. 
                
                val navController = rememberNavController()
                // startDestination hangi görünümle/ekranla başlanacağı
                NavHost(navController = navController, startDestination = "crypto_list_screen") {

                    // NavHost içinde kullanacağım composableları burada yazabiliyorum.

                    composable("crypto_list_screen") {
                        // CryptoListScreen
                        CryptoListScreen(navController = navController)
                    }

                    // Composable ın alacağı argümanları belitiyoruz.
                    composable("crypto_detail_screen/{cryptoId}/{cryptoPrice}",arguments = listOf(
                        navArgument("cryptoId") {
                            // Argümanın tipi
                            type = NavType.StringType
                        },
                        navArgument("cryptoPrice") {
                            type = NavType.StringType
                        }
                    )) {
                        // argümanları değişkenlere atıyoruz
                        val cryptoId = remember {
                            it.arguments?.getString("cryptoId")
                        }

                        val cryptoPrice = remember {
                            it.arguments?.getString("cryptoPrice")
                        }
                        
                        CryptoDetailScreen(id = cryptoId ?: "", price = cryptoPrice ?: "", navController = navController)
                    }
                }
            }
        }
    }
}

