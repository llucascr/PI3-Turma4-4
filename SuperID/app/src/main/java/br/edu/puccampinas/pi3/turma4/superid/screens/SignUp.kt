package br.edu.puccampinas.pi3.turma4.superid.screens

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.HomeActivity
import br.edu.puccampinas.pi3.turma4.superid.functions.SignUpViewModel
import br.edu.puccampinas.pi3.turma4.superid.functions.saveName
import br.edu.puccampinas.pi3.turma4.superid.functions.validationSingUp
import br.edu.puccampinas.pi3.turma4.superid.functions.validationUtils
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun SignUpFormScreen(navController: NavController, viewModel: SignUpViewModel = viewModel()) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    val agreeTerms by viewModel.agreeTerms.collectAsState()
    var triedToSubmit by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Ícone de Voltar no canto superior esquerdo
            IconButton(
                onClick = { navController.navigate("singin") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(21.dp)
                    .height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Cadastro",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.onNameChange(it) },
                placeholder = { Text("Nome", color = Color.Gray, fontSize = 16.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = nameError,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            if (nameError) {
                Text(
                    text = "Nome inválido",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                placeholder = { Text("Email", color = Color.Gray, fontSize = 16.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = emailError,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            if (emailError) {
                Text(
                    text = "E-mail inválido",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = { Text("Senha", color = Color.Gray, fontSize = 16.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = passwordError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            if (passwordError) {
                Text(
                    text = "Senha inválida",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Terms
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeTerms,
                    onCheckedChange = { viewModel.onAgreeTermsChange(it) },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = "Eu concordo com os  ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = "Termos de Uso",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable { navController.navigate("terms") }
                )
            }
            if (triedToSubmit &&!agreeTerms) {
                Text(
                    text = "Aceitar Termos de uso!",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Create Account Button
            Button(
                onClick = {
                    nameError = validationUtils.emptyRegistrationFields(name, email, password)
                    emailError = validationUtils.emailValidation(email)
                    passwordError = validationUtils.passwordInvalid(password)
                    triedToSubmit = true

                    if (!nameError && !emailError && !passwordError && agreeTerms) {
                        try {
                            validationSingUp(
                                context,
                                name,
                                email,
                                password,
                                onSuccess = {
                                    val intent = Intent(context, HomeActivity::class.java)
                                    context.startActivity(intent)
                                },
                                onFailure = { e ->
                                    Log.e("SINGUP", "ERRO AO CRIAR A CONTA: ${e.message}")
                                }
                            )
                            saveName(context, name)
                        } catch (e: Exception) {
                            Log.e("SINGUP", "Erro inesperado: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text("Criar Conta", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sign In link
            Row {
                Text(
                    text = "Já tem uma conta? ",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Entrar",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { navController.navigate("singin") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingUpFormScreenPreview() {
    SuperIDTheme(darkTheme = false, dynamicColor = false) {
        SignUpFormScreen(navController = rememberNavController())
    }
}