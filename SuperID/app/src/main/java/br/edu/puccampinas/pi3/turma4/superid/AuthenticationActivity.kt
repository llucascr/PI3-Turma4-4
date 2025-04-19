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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.screens.SingInFormScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.SingUpFormScreen
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

class AuthenticationActivity : ComponentActivity() {

//    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

//        auth = Firebase.auth
//        db = Firebase.firestore

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
        composable("singin") { SingInFormScreen(navController) }
        composable("singup") { SingUpFormScreen(navController) }
    }
}