package br.edu.puccampinas.pi3.turma4.superid.screens

import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.edu.puccampinas.pi3.turma4.superid.R

@Composable
fun BottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /* ação home */ },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.iconhome),
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
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = {
                Text("QR Code", color = MaterialTheme.colorScheme.onBackground)
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
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = {
                Text("Perfil", color = MaterialTheme.colorScheme.onBackground)
            }
        )
    }
}