package br.edu.puccampinas.pi3.turma4.superid.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.RadioButtonDefaults.colors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.HomeActivity
import com.google.firebase.firestore.FirebaseFirestore
import br.edu.puccampinas.pi3.turma4.superid.R
import br.edu.puccampinas.pi3.turma4.superid.WelcomeActivity
import br.edu.puccampinas.pi3.turma4.superid.functions.verifyInputs
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddPwUI(navController: NavController, categoryName: String) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ){
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
            MainContent(categoryName)
        }
    }
}



@Composable
fun MainContent(categoryName: String){
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Ícone de Voltar no canto superior esquerdo
            IconButton(
                onClick = {
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(0.dp,21.dp,0.dp,0.dp)
                    .height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
    NewPasswordForms(categoryName)
}
@Composable
fun NewPasswordForms(categoryName: String){
    var showSuccessDialog by remember { mutableStateOf(false) }
    var success by remember {mutableStateOf(false)}
    var failure by remember {mutableStateOf(true)}
    Spacer(modifier = Modifier.size(50.dp))
    Box(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally){
            Text("Nova Senha", textAlign = TextAlign.Center, fontSize = 30.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.size(25.dp))
            Box(modifier = Modifier.fillMaxWidth()){
                Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {

                    var titulo by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        placeholder = { Text("Título")},
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                            focusedContainerColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    var descricao by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        placeholder = { Text("Descrição") },
                        singleLine = false,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                            focusedContainerColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                    var url by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        placeholder = { Text("Url")},
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                            focusedContainerColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                    var login by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = login,
                        onValueChange = { login = it },
                        placeholder = { Text("Login") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                            focusedContainerColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    var senha by remember { mutableStateOf("") }
                    var passwordVisible by remember {mutableStateOf(false)}

                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        placeholder = { Text("Senha") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                            focusedContainerColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )
                    if(success){
                        Text("Senha salva!", color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                        // Toast de erro
                    }
                    if(!failure){
                        Spacer(modifier = Modifier.size(15.dp))
                        Text("Campos inválidos!", color = MaterialTheme.colorScheme.error, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.size(45.dp))
                    Button(onClick = {/*TODO: FUN SAVE PW*/
                       val savingStatus: Boolean = verifyInputs(titulo,descricao,login,senha,url,categoryName)
                        if(savingStatus) success = true else failure = false
                    }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)){
                        Text("Salvar", color = MaterialTheme.colorScheme.onSecondary)
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AddPwPreview() {
//    SuperIDTheme {
//        SuperIDTheme(darkTheme = true, dynamicColor = false) {
//            AddPwUI(navController = rememberNavController())
//        }
//    }
//}
