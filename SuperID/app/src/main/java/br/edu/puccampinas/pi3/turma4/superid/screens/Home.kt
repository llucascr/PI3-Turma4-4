package br.edu.puccampinas.pi3.turma4.superid.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.functions.createCategory
import br.edu.puccampinas.pi3.turma4.superid.functions.getCategorys
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    var categoryList by remember { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getCategorys { categories ->
            categoryList = categories
        }
    }

    val groupedItems = categoryList.chunked(2)

    Scaffold(
        containerColor = colorScheme.background,
        bottomBar = {
            Divider(color = colorScheme.onBackground, thickness = 4.dp)
            BottomBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                containerColor = colorScheme.onSecondary,
                shape = CircleShape,
                modifier = Modifier
                    .height(72.dp)
                    .width(72.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Adicionar categoria",
                    modifier = Modifier.size(32.dp),
                    tint = colorScheme.primary
                )
            }
            CategoryInputDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onConfirm = { nomeCategoria ->
                    createCategory(context, nomeCategoria){ success ->
                        if (success) {
                            getCategorys { categories ->
                                categoryList = categories
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Home",
                    color = colorScheme.onBackground,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Procurar categoria", color = colorScheme.onSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = colorScheme.onSecondary
                        )
                    },
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
            }


            groupedItems.forEach { rowItems ->
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { (label, count) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colorScheme.primary)
                                    .padding(12.dp)
                                    .clickable {
                                        navController.navigate("passwordsByCategory/${label}")
                                    }
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = "$label ($count)",
                                        color = colorScheme.onPrimary,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = label,
                                        color = colorScheme.onPrimary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryInputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Text(
                    text = "Confirmar",
                    modifier = Modifier
                        .clickable {
                            onConfirm(text)
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
                    text = "Nova Categoria",
                    color = colorScheme.onPrimary

                )
                    },
            text = {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Digite o nome da categoria") },
                    singleLine = true
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SuperIDTheme(darkTheme = true, dynamicColor = false) {
        HomeScreen(navController = rememberNavController())
    }
}