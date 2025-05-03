package br.edu.puccampinas.pi3.turma4.superid.screens

import android.content.Intent
import android.util.Log
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.CategoryActivity
import br.edu.puccampinas.pi3.turma4.superid.functions.validationSingUp
import br.edu.puccampinas.pi3.turma4.superid.functions.validationUtils
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors.backgroundColor
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors.inputBackground
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors.textColor
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun SingUpFormScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var agreeTerms by remember { mutableStateOf(false) }
    var triedToSubmit by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = SingInColors.primaryGreen
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Name", color = Color.Gray, fontSize = 16.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = nameError,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = inputBackground,
                    focusedContainerColor = inputBackground,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            if (nameError) {
                Text(
                    text = "Nome inválido",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color.Gray, fontSize = 16.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = emailError,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = inputBackground,
                    focusedContainerColor = inputBackground,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            if (emailError) {
                Text(
                    text = "E-mail inválido",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = Color.Gray, fontSize = 16.sp) },
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
                    unfocusedContainerColor = inputBackground,
                    focusedContainerColor = inputBackground,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            if (passwordError) {
                Text(
                    text = "Password inválido",
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
                Checkbox( // Só deixar o usuario criar a conta se ele aceitar os termos de uso
                    checked = agreeTerms,
                    onCheckedChange = { agreeTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = SingInColors.primaryGreen)
                )
                Text(
                    text = "I agree to the ",
                    fontSize = 14.sp,
                    color = textColor
                )
                Text(
                    text = "Terms",
                    color = SingInColors.primaryGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("terms") }
                )
                Text(
                    text = " and ",
                    fontSize = 14.sp,
                    color = textColor
                )
                Text(
                    text = "Privacy Policy",
                    color = SingInColors.primaryGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }
            if (triedToSubmit &&!agreeTerms) {
                Text(
                    text = "Aceitar Termos de uso!",
                    color = Color.Red,
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
                                    val intent = Intent(context, CategoryActivity::class.java)
                                    context.startActivity(intent)
                                },
                                onFailure = { e ->
                                    Log.e("SINGUP", "ERRO AO CRIAR A CONTA: ${e.message}")
                                }
                            )
                        } catch (e: Exception) {
                            Log.e("SINGUP", "Erro inesperado: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SingInColors.primaryGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text("Create Account", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sign In link
            Row {
                Text(
                    text = "Do you have an account? ",
                    color = textColor,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign In",
                    color = SingInColors.primaryGreen,
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
    SuperIDTheme {
        SingUpFormScreen(navController = rememberNavController())
    }
}