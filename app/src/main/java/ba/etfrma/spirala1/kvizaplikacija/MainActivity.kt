package ba.etfrma.spirala1.kvizaplikacija

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ba.etfrma.spirala1.kvizaplikacija.navigation.AppNavigation
import ba.etfrma.spirala1.kvizaplikacija.ui.theme.KvizAplikacijaTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KvizAplikacijaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // NavController se kreira ovdje i živi dok Activity živi
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}