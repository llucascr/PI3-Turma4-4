package br.edu.puccampinas.pi3.turma4.superid.screens

import android.content.Intent
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.HomeActivity
import br.edu.puccampinas.pi3.turma4.superid.QrCodeActivity
import br.edu.puccampinas.pi3.turma4.superid.R

@Composable
fun BottomBar(navController: NavController) {
    val context = LocalContext.current
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate("home")},
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = {
                Text("Home", color = MaterialTheme.colorScheme.onBackground)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onBackground,
                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                indicatorColor = Color.Transparent,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                val intent = Intent(context, QrCodeActivity::class.java)
                context.startActivity(intent)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "QR Code",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(30.dp),
                )
            },
            label = {
                Text("QR Code", color = MaterialTheme.colorScheme.onBackground)
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("profile") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = {
                Text("Perfil", color = MaterialTheme.colorScheme.onBackground)
            }
        )
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    BottomBar(navController = rememberNavController())
}