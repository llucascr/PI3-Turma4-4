package br.edu.puccampinas.pi3.turma4.superid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.screens.HomeScreen
import br.edu.puccampinas.pi3.turma4.superid.screens.ProfileScreen
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperIDTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashNav(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SplashNav(modifier: Modifier = Modifier) {
    LaunchedEffect(true) {
        delay(2000)
    }

    val context = LocalContext.current
    var intent = Intent(context, WelcomeActivity::class.java)
    val user = Firebase.auth.currentUser

    if (user != null) { // se o usuario estiver logado
        intent = Intent(context, AuthenticationActivity::class.java)
    }

    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuperIDTheme {}
}