package br.edu.puccampinas.pi3.turma4.superid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.screens.PasswordsByCategoryScreen
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import kotlin.toString

class PasswordByCategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val categoryName = intent.getStringExtra("categoryName")
        setContent {
            SuperIDTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PasswordsScreen(modifier = Modifier, categoryName)
                }
            }
        }
    }
}

@Composable
fun PasswordsScreen(modifier: Modifier, categoryName: String?) {
    //val db = Firebase.firestore
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "PasswordByCategory") {
        composable("PasswordByCategory") {
            PasswordsByCategoryScreen(categoryName.toString(), navController) }
    }
}

