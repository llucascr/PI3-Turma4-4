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
import br.edu.puccampinas.pi3.turma4.superid.functions.validationSingIn
import br.edu.puccampinas.pi3.turma4.superid.functions.validationUtils
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors.backgroundColor
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors.inputBackground
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors.textColor
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun SingInFormScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

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
            // Título
            Text(
                text = "Sign In",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = SingInColors.primaryGreen
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (validationUtils.checkUserAuth(context)) {
                email = validationUtils.getSavedEmail(context).toString()
                Row {
                    Column {
                        Text(
                            text = email,
                            color = Color.White,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Text(
                            text = "Logout",
                            color = SingInColors.primaryGreen,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .clickable {
                                    validationUtils.logoutUser(context)
                                }
                        )
                    }
                }
            } else {
                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email", color = Color.Gray, fontSize = 16.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
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
            }

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
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            imageVector = if (passwordVisible.value)
                                Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
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
                    text = "Senha com menos de 6 caracteres",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = {
                    emailError = validationUtils.emailValidation(email)
                    passwordError = validationUtils.passwordInvalid(password)
                    if (!emailError && !passwordError) {
                        validationSingIn(
                            context,
                            email,
                            password,
                            onSuccess = {
                                val intent = Intent(context, CategoryActivity::class.java)
                                context.startActivity(intent)
                            },
                            onFailure = { e ->
                                Log.e("SINGIN", "ERRO AO FAZER LOGIN: ${e.message}")
                            }
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SingInColors.primaryGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text("Log In", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sign Up
            Row {
                Text(
                    text = "Don't have account? ",
                    color = textColor,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign Up",
                    color = SingInColors.primaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("singup")
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthenticationNavPreviwe() {
    SuperIDTheme {
        SingInFormScreen(navController = rememberNavController())
    }
}