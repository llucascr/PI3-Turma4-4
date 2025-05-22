package br.edu.puccampinas.pi3.turma4.superid.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.DarkBackground
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme


@Composable
fun PasswordDetailsScreen(
    title: String,
    email: String,
    password: String,
    website: String,
    description: String,
    navController: NavController
    //onBackClick: () -> Unit,
    //onEditClick: () -> Unit,
    //onDeleteClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Top bar com botão de voltar
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {/* ação voltar */}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorScheme.onBackground
                    )
                }
                Text(
                    text = title,
                    style = typography.titleLarge,
                    color = colorScheme.onBackground,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Informações da senha
            InfoItem(label = "Nome", value = email)
            InfoItem(label = "Senha", value = password, showEyeIcon = true)
            InfoItem(label = "WebSite", value = website)
            InfoItem(label = "Descrição", value = description)

            Spacer(modifier = Modifier.height(32.dp))

            // Botões de ação
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ActionButton(
                    text = "Editar",
                    color = colorScheme.primary,
                    onClick = {/*editar senha*/} //onEditClick
                )
                ActionButton(
                    text = "Excluir",
                    color = colorScheme.error,
                    onClick = {/*excluir senha*/} //onDeleteClick
                )
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, showEyeIcon: Boolean = false) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = "$label:",
            style = typography.bodyLarge,
            color = colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = typography.bodyMedium,
                color = colorScheme.onBackground
            )
            /*if (showEyeIcon) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.VisibilityOff,
                    contentDescription = "Ocultar/Mostrar senha",
                    tint = colorScheme.onBackground
                )
            }*/
        }
    }
}

@Composable
fun ActionButton(text: String, color: Color, onClick: () -> Unit) {
    androidx.compose.material3.Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordDetailsScreenPreview() {
    SuperIDTheme(darkTheme = true, dynamicColor = false) {
        PasswordDetailsScreen(
            title = "Conta google",
            email = "teste@email.com",
            password = "minhaSenha123",
            website = "https://www.google.com",
            description = "exemplo de descrição",
            navController = rememberNavController()
            //onBackClick = {},
            //onEditClick = {},
            //onDeleteClick = {}
        )
    }
}