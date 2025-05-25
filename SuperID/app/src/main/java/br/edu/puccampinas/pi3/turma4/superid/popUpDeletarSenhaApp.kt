package br.edu.puccampinas.pi3.turma4.superid

import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class popUpDeletarSenhaApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperIDTheme {
                PopUp()
            }
        }
    }
}

@Preview
@Composable
fun PopUp() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isDisplayDialog by remember { mutableStateOf(true) }

    if (isDisplayDialog) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .size(250.dp)
                    .background(color = Color.White)
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Alerta",
                    tint = Color.Red
                )

                Text(
                    text = "Essa ação é permanente.",
                    color = Color(0xFF661B16),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = {
                        val db = FirebaseFirestore.getInstance()
                        val usuarioId = "ugjUYTnL6wYIdQx3Mx216wxrtL22"
                        val categoria = "Sites Web"
                        val senhaId = "CPanVhlx8zNYWLYBSEj4"

                        db.collection("users")
                            .document(usuarioId)
                            .collection("categorias")
                            .document(categoria)
                            .collection("senhas")
                            .document(senhaId)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("TesteFire", "DocumentSnapshot successfully deleted!")
                                scope.launch {
                                    snackbarHostState.showSnackbar("Senha deletada com sucesso!")
                                }
                                isDisplayDialog = false
                            }
                            .addOnFailureListener { e ->
                                Log.w("TesteFire", "Erro ao deletar documento", e)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Erro ao deletar senha.")
                                }
                                isDisplayDialog = false
                            }
                    },
                    modifier = Modifier
                        .width(220.dp)
                        .padding(top = 35.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF166534),
                        contentColor = Color.White
                    )
                ) {
                    Text("Confirmar")
                }

                Button(
                    onClick = {
                        isDisplayDialog = false
                    },
                    modifier = Modifier.width(220.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF661B16),
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancelar")
                }
            }
        }
    }

    // Exibir snackbar
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
    }
}