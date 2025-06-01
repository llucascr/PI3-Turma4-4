package br.edu.puccampinas.pi3.turma4.superid

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.puccampinas.pi3.turma4.superid.screens.HomeScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.PasswordsByCategoryScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.ProfileScreen
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import br.edu.puccampinas.pi3.turma4.superid.screens.PasswordDetailsScreen

class HomeActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperIDTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeNav(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable(
            "passwordsByCategory/{categoryId}/{isDefault}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("isDefault") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val isDefault = backStackEntry.arguments?.getBoolean("isDefault") ?: false
            PasswordsByCategoryScreen(
                categoryId = categoryId,
                isDefault = isDefault,
                navController = navController
            )
        }

        composable(
            route = "passwordDetails/{categoryId}/{documentId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("documentId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val documentId = backStackEntry.arguments?.getString("documentId") ?: ""

            PasswordDetailsScreen(
                categoryId = categoryId,
                documentId = documentId,
                navController = navController
            )
        }

        composable(
            route = "passwordDetails/{categoryId}/{documentId}/editarSenha",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("documentId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val documentId = backStackEntry.arguments?.getString("documentId") ?: ""

            AlterarSenha(
                categoryId,
                documentId,
                navController
            )
        }
    }
}
