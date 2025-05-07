package br.edu.puccampinas.pi3.turma4.superid.screens

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.edu.puccampinas.pi3.turma4.superid.R

@Composable
fun BottomBar() {
    BottomAppBar(
        containerColor = Color.Black
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /* ação home */ },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.iconhome),
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp),
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
            onClick = { /* ação autenticação */ },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.iconqrcode),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer(
                            scaleX = 3.4f,
                            scaleY = 3.4f
                        ),
                    tint = Color.White
                )
            },
            label = {
                Text("QR Code", color = Color.White)
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = { /* ação perfil */ },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.iconperson),
                    contentDescription = "Perfil",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            },
            label = {
                Text("Perfil", color = Color.White)
            }
        )
    }
}