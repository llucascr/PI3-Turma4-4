package br.edu.puccampinas.pi3.turma4.superid.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.*
import br.edu.puccampinas.pi3.turma4.superid.deletarSenha
import br.edu.puccampinas.pi3.turma4.superid.functions.getPasswordDetails


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PasswordDetailsScreen(
    categoryId: String,
    documentId: String,
    navController: NavController
) {
    val context = LocalContext.current
    var password by remember { mutableStateOf<PasswordItemDetails?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(context, categoryId, documentId) {
        getPasswordDetails(context, categoryId, documentId) { result ->
            password = result
        }
    }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorScheme.onBackground
                    )
                }
                Text(
                    text = password?.title ?: "Carregando...",
                    style = typography.titleLarge,
                    color = colorScheme.onBackground,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (showDeleteDialog) {
                PasswordDeleteDialog(
                    showDialog = showDeleteDialog,
                    onDismiss = { showDeleteDialog = false },
                    onConfirm = {
                        deletarSenha(context, categoryId, documentId) { success ->
                            if (success) {
                                navController.popBackStack()
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            password?.let { data ->
                InfoItem(label = "Login", value = data.login)
                InfoItem(label = "Senha", value = data.password)
                InfoItem(label = "WebSite", value = data.url)
                InfoItem(label = "Descrição", value = data.description)

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ActionButton(
                        text = "Editar",
                        color = colorScheme.primary,
                        onClick = { navController.navigate("passwordDetails/$categoryId/$documentId/editarSenha") }
                    )
                    ActionButton(
                        text = "Excluir",
                        color = colorScheme.error,
                        onClick = {
                            showDeleteDialog = true
                        }
                    )
                }
            } ?: Text(
                text = "Carregando dados...",
                color = colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
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

data class PasswordItemDetails(
    val login: String,
    val password: String,
    val url: String,
    val description: String,
    val title: String
)

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

@Composable
fun PasswordDeleteDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Text(
                    text = "Confirmar",
                    modifier = Modifier
                        .clickable {
                            onConfirm()
                            onDismiss()
                        }
                        .padding(8.dp),
                    color = colorScheme.primary
                )
            },
            dismissButton = {
                Text(
                    text = "Cancelar",
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(8.dp),
                    color = colorScheme.error
                )
            },
            title = {
                Text(
                    text = "Excluir Senha",
                    color = colorScheme.onBackground
                )
            },
            text = {
                Text(
                    text = "Tem certeza que deseja excluir esta senha? A senha será excluída permanentemente.",
                    color = colorScheme.onBackground
                )
            }
        )
    }
}

/*@Preview(showBackground = true)
@Composable
fun PasswordDetailsScreenPreview() {
    SuperIDTheme(darkTheme = true, dynamicColor = false) {
        PasswordDetailsScreen(

        )
    }
}*/