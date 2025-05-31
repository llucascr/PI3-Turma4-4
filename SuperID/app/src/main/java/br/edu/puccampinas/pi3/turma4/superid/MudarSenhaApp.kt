package br.edu.puccampinas.pi3.turma4.superid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

import kotlinx.coroutines.launch


import com.google.firebase.firestore.FirebaseFirestore

//Imports de icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ArrowBack

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.edu.puccampinas.pi3.turma4.superid.functions.encrypt
import br.edu.puccampinas.pi3.turma4.superid.functions.getPasswordDetails
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


fun atualizarDadosFirestore(
    context: android.content.Context,
    categoryname: String,
    documentId: String,
    titulo: String,
    descricao: String,
    login: String,
    senha: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val usuarioId = Firebase.auth.currentUser?.uid.toString()
    val senhaId = documentId

    val dadosAtualizados = hashMapOf(
        "titulo" to titulo,
        "descricao" to descricao,
        "login" to login,
        "senha" to senha
    )

    db.collection("users")
        .document(usuarioId)
        .collection("categorias")
        .document(categoryname)
        .collection("senhas")
        .document(senhaId)
        .set(dadosAtualizados)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { e ->
            onError(e.message ?: "Erro")
        }
}

fun puxarDados(
    categoryName: String,
    documentId: String,
    onResult: (String, String, String, String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val usuarioId = Firebase.auth.currentUser?.uid.toString()
    val categoria = categoryName
    val senhaId = documentId

    db.collection("users")
        .document(usuarioId)
        .collection("categorias")
        .document(categoria)
        .collection("senhas")
        .document(senhaId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val titulo = document.getString("titulo") ?: ""
                val descricao = document.getString("descricao") ?: ""
                val login = document.getString("login") ?: ""
                val senha = document.getString("senha") ?: ""
                Log.d("Firestore", "Dados carregados com sucesso.")
                onResult(titulo, descricao, login, senha)
            } else {
                Log.d("Firestore", "Documento não encontrado.")
            }
        }
        .addOnFailureListener { exception ->
            Log.w("Firestore", "Erro ao buscar o documento", exception)
        }
}

@Composable
fun AlterarSenha(
    categoryname: String,
    documentId: String,
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var nova_senha by remember { mutableStateOf("") }
    var conf_nova_senha by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Carrega os dados do Firestore
    LaunchedEffect(Unit) {
        puxarDados(categoryname, documentId) { t, d, l, s ->
            titulo = t
            descricao = d
            login = l
            nova_senha = s
            conf_nova_senha = ""
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    modifier = Modifier
                        .padding(bottom = 40.dp),
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.Start
        ) {
            HeaderAlterarSenhas(navController, categoryname, documentId)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                TextFieldsAlterarSenhas(
                    titulo = titulo,
                    onTituloChange = { titulo = it },
                    descricao = descricao,
                    onDescricaoChange = { descricao = it },
                    login = login,
                    onLoginChange = { login = it },
                    nova_senha = nova_senha,
                    onNovaSenhaChange = { nova_senha = it },
                    conf_nova_senha = conf_nova_senha,
                    onConfNovaSenhaChange = { conf_nova_senha = it }
                )
            }

            Box(
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(10.dp),
                        ambientColor = Color(0xFFB0FFB0),
                        spotColor = Color(0xFFB0FFB0)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                BotaoSalvar(
                    onClick = {
                        if (nova_senha == conf_nova_senha) {
                            nova_senha = encrypt(Firebase.auth.currentUser.toString(), nova_senha)
                            atualizarDadosFirestore(context, categoryname, documentId,titulo, descricao, login, nova_senha,
                                onSuccess = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Senha salva com sucesso!", "OK")
                                    }
                                },
                                onError = { errorMsg ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Erro ao salvar: $errorMsg", "OK")
                                    }
                                }
                            )
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("As senhas não coincidem", "TENTE NOVAMENTE")
                            }
                        }
                    })
            }
            Spacer(
                Modifier
                    .height(50.dp)
            )
        }
    }
}

@Composable
fun HeaderAlterarSenhas(
    navController: NavController,
    categoryName: String,
    documentId: String
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight(0.1f),
        verticalArrangement = Arrangement.Bottom
    ) {
        IconButton(
            onClick = {navController.navigate("passwordDetails/$categoryName/$documentId")},
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .height(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text("Alterar Senha", color = MaterialTheme.colorScheme.primary, fontSize = 32.sp)
    }
}

@Composable
fun TextFieldsAlterarSenhas(
    titulo: String,
    onTituloChange: (String) -> Unit,
    descricao: String,
    onDescricaoChange: (String) -> Unit,
    login: String,
    onLoginChange: (String) -> Unit,
    nova_senha: String,
    onNovaSenhaChange: (String) -> Unit,
    conf_nova_senha: String,
    onConfNovaSenhaChange: (String) -> Unit,
) {
    val verde = Color(0xFF166534)
    val branco = Color(0xFFFFFFFF)
    val preto = Color(0xFF000000)

    val commonModifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp)
        .height(50.dp)

    OutlinedTextField(
        value = titulo,
        onValueChange = onTituloChange,
        placeholder = { Text("Título", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = preto,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier,
        shape = RoundedCornerShape(10.dp)
    )

    OutlinedTextField(
        value = descricao,
        onValueChange = onDescricaoChange,
        placeholder = { Text("Descrição", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = preto,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier,
        shape = RoundedCornerShape(10.dp)
    )

    OutlinedTextField(
        value = login,
        onValueChange = onLoginChange,
        placeholder = { Text("Login", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = preto,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier,
        shape = RoundedCornerShape(10.dp)
    )

    OutlinedTextField(
        value = nova_senha,
        onValueChange = onNovaSenhaChange,
        placeholder = { Text("Nova senha", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = preto,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier,
        shape = RoundedCornerShape(10.dp)
    )

    OutlinedTextField(
        value = conf_nova_senha,
        onValueChange = onConfNovaSenhaChange,
        placeholder = { Text("Confirmar nova senha", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = preto,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier,
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun BotaoSalvar(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxSize(),
        colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(60.dp)
    ) {
        Text(
            "Salvar", color = Color.White, fontSize = 20.sp
        )
    }
}