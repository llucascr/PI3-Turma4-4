package br.edu.puccampinas.pi3.turma4.superid.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import kotlinx.coroutines.delay

@Composable
fun PopUpScreen(message: String, icon: ImageVector, color: Color, onDismiss: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = 8.dp,
            contentColor = Color.White,
            modifier = Modifier.padding(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }

    // Delay e chamada de onDismiss
    LaunchedEffect(Unit) {
        delay(1000)
        onDismiss()
    }
}

@Composable
fun AutoDismissPopup(
    message: String,
    icon: ImageVector,
    iconColor: Color,
    durationMillis: Long = 3000,
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(durationMillis)
        onDismiss()
    }

    PopUpScreen(
        message = message,
        icon = icon,
        color = iconColor,
        onDismiss = onDismiss,
    )
}
