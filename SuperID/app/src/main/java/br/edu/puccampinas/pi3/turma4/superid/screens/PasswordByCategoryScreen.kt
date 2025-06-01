package br.edu.puccampinas.pi3.turma4.superid.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.functions.getPasswordsByCategory
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.mutableIntStateOf
import br.edu.puccampinas.pi3.turma4.superid.QrCodeActivity
import br.edu.puccampinas.pi3.turma4.superid.SavePasswordActivity
import br.edu.puccampinas.pi3.turma4.superid.functions.deleteCategory
import br.edu.puccampinas.pi3.turma4.superid.functions.getCategoryById

@Composable
fun PasswordsByCategoryScreen(
    categoryId: String,
    isDefault: Boolean,
    navController: NavController
) {
    val context = LocalContext.current
    var passwords by remember { mutableStateOf<List<PasswordItem>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    //var refreshTrigger by remember { mutableIntStateOf(0) }

    var categoryName by remember { mutableStateOf("Carregando...") }

    LaunchedEffect(categoryId) {
        getCategoryById(categoryId) { fetchedName ->
            categoryName = fetchedName ?: "Categoria não encontrada"
        }
        getPasswordsByCategory(context, categoryId) { result ->
            passwords = result
        }
    }

    Scaffold(
        containerColor = colorScheme.background,
        bottomBar = { BottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, SavePasswordActivity::class.java)
                    intent.putExtra("categoryId", categoryId)
                    context.startActivity(intent)
                },
                containerColor = colorScheme.onSecondary,
                shape = CircleShape,
                modifier = Modifier
                    .height(72.dp)
                    .width(72.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Adicionar senha",
                    modifier = Modifier.size(32.dp),
                    tint = colorScheme.primary
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Bar com voltar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = colorScheme.onBackground
                        )
                    }

                    Text(
                        text = categoryName,
                        style = typography.titleLarge,
                        color = colorScheme.onBackground,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { /* TODO: ação de editar */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar categoria",
                            tint = colorScheme.onBackground,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Excluir categoria",
                            tint = colorScheme.onBackground,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de busca
            OutlinedTextField(
                value = "",
                onValueChange = { /* TODO: Filtro de busca */ },
                placeholder = { Text("Procurar senha", color = colorScheme.onSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = colorScheme.onSecondary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = colorScheme.secondary,
                    focusedContainerColor = colorScheme.secondary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(passwords) { password ->
                    PasswordCard(password, onClick = {
                        navController.navigate("passwordDetails/${categoryId}/${password.documentId}")
                    })
                }
            }

            CategoryDeleteDialog(
                showDialog = showDeleteDialog,
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    deleteCategory(context, categoryId) { success ->
                        if (success) {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun PasswordCard(item: PasswordItem, onClick: () -> Unit) {

    Card(
        colors = CardDefaults.cardColors(containerColor = colorScheme.primary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.title,
                    style = typography.bodyLarge,
                    color = colorScheme.onPrimary
                )
                Text(
                    text = item.description,
                    style = typography.bodyMedium,
                    color = colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

data class PasswordItem(
    val documentId: String,
    val title: String,
    val description: String
)

@Composable
fun CategoryDeleteDialog(
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
                    text = "Excluir Categoria",
                    color = colorScheme.onBackground
                )
            },
            text = {
                Text(
                    text = "Tem certeza que deseja excluir esta categoria? Todas as senhas dentro dela também serão excluídas permanentemente.",
                    color = colorScheme.onBackground
                )
            }
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PasswordsByCategoryScreenPreview() {
//    SuperIDTheme(darkTheme = true, dynamicColor = false) {
//        PasswordsByCategoryScreen(
//            categoryName = "Place",
//            navController = rememberNavController()
//        )
//    }
//}