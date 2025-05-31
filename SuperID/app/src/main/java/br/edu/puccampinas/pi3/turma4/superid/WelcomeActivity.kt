package br.edu.puccampinas.pi3.turma4.superid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.screens.AuthTutorialScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.CategoriaTutorialScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.PasswordTutorialScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.WelcomeFinishScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.WelcomeScreen
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperIDTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeNav(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WelcomeNav(modifier: Modifier) {
    val navController = rememberNavController()
    val contex = LocalContext.current

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("categoriaTutorialScreen") { CategoriaTutorialScreen(navController) }
        composable("passwordTutorialScreen") { PasswordTutorialScreen(navController) }
        composable("authTutorialScreen") { AuthTutorialScreen(navController) }
        composable("welcomeFinish") { WelcomeFinishScreen(navController) }
    }

}