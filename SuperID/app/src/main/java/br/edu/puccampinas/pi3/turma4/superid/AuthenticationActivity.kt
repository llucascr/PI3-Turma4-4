package br.edu.puccampinas.pi3.turma4.superid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.screens.SignInFormScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.SignUpFormScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.TermsOfUseScreen
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

class AuthenticationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperIDTheme {
                AuthenticationNav()
            }
        }
    }
}

@Composable
fun AuthenticationNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "singin") {
        composable("singin") { SignInFormScreen(navController) }
        composable("singup") { SignUpFormScreen(navController) }
        composable("terms") { TermsOfUseScreen(navController) }
    }
}