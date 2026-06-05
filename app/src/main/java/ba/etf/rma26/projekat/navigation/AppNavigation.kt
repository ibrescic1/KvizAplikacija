package ba.etf.rma26.projekat.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ba.etf.rma26.projekat.ui.screens.FilterScreen
import ba.etf.rma26.projekat.ui.screens.KvizTakeScreen
import ba.etf.rma26.projekat.ui.screens.KvizoviScreen
import ba.etf.rma26.projekat.viewmodel.KvizTakeViewModel
import ba.etf.rma26.projekat.viewmodel.KvizViewModel
import ba.etf.rma26.projekat.viewmodel.UpisViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {

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

        composable(
            route = NavRoutes.KVIZ_TAKE_SCREEN,
            arguments = listOf(navArgument("idKviza") { type = NavType.IntType })
        ) { backStackEntry ->
            val idKviza = backStackEntry.arguments?.getInt("idKviza") ?: return@composable
            val kvizTakeViewModel: KvizTakeViewModel = viewModel()
            KvizTakeScreen(
                idKviza = idKviza,
                viewModel = kvizTakeViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}