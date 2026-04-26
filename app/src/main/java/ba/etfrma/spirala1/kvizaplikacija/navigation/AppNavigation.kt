package ba.etfrma.spirala1.kvizaplikacija.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ba.etfrma.spirala1.kvizaplikacija.ui.screens.FilterScreen
import ba.etfrma.spirala1.kvizaplikacija.ui.screens.KvizoviScreen
import ba.etfrma.spirala1.kvizaplikacija.viewmodel.KvizViewModel
import ba.etfrma.spirala1.kvizaplikacija.viewmodel.UpisViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {

    // Dijeljeni ViewModeli — isti instance na oba ekrana
    // viewModel() bez parametra = scoped na NavBackStackEntry
    // Da bismo dijelili isti VM između ekrana, koristimo activity scope
    val kvizViewModel: KvizViewModel = viewModel()
    val upisViewModel: UpisViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.FILTER_SCREEN
    ) {
        composable(NavRoutes.FILTER_SCREEN) {
            FilterScreen(
                kvizViewModel = kvizViewModel,
                upisViewModel = upisViewModel,
                onPrikaziKvizove = {
                    navController.navigate(NavRoutes.KVIZOVI_SCREEN)
                }
            )
        }

        composable(NavRoutes.KVIZOVI_SCREEN) {
            KvizoviScreen(
                kvizViewModel = kvizViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}