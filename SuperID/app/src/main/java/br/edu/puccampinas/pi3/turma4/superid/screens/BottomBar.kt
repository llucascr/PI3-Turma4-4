package br.edu.puccampinas.pi3.turma4.superid.screens

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.R

@Composable
fun BottomBar(navController: NavController) {
    Divider(color = Color.White, thickness = 4.dp)
    BottomAppBar(
        containerColor = Color.Black
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate("categoty")},
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            },
            label = {
                Text("Home", color = Color.White)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color.Transparent,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {  },
            icon = {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "QR Code",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            },
            label = {
                Text("QR Code", color = Color.White)
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("profile") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            },
            label = {
                Text("Perfil", color = Color.White)
            }
        )
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    BottomBar(navController = rememberNavController())
}