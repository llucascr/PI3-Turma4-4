package br.edu.puccampinas.pi3.turma4.superid.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.AuthenticationActivity
import br.edu.puccampinas.pi3.turma4.superid.SavePasswordActivity
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun MainScreen(navController: NavController) {
    val contex = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Olá, User!")
        Button(onClick = {
            var intent = Intent(contex, SavePasswordActivity::class.java)
            contex.startActivity(intent)
        }) {
            Text("Go to add password!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SuperIDTheme {
        MainScreen(navController = rememberNavController())
    }
}