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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.screens.AddPwUI
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class SavePasswordActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryId = intent.getStringExtra("categoryId")
        enableEdgeToEdge()
        setContent {
            SuperIDTheme{
              SavePasswordScreen(modifier = Modifier, categoryId.toString())
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavePasswordScreen(modifier: Modifier, categoryId: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "saveNewPassword") {
        composable("saveNewPassword") { AddPwUI(navController, categoryId) }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Preview() {
//    SavePasswordScreen(modifier = Modifier, categoryId = "Sites Web")
//}
