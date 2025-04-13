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
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.MainActivity
import br.edu.puccampinas.pi3.turma4.superid.functions.creatAccount
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingUpColors
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun SingUpFormScreen(navController: NavController) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreeTerms by remember { mutableStateOf(false) }

    var context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text("Sign Up", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A5EFF))

        Spacer(modifier = Modifier.height(12.dp))

//        Text(
//            "Welcome to de SuperID",
//            textAlign = TextAlign.Center,
//            fontSize = 14.sp,
//            color = Color.Gray
//        )

//        Spacer(modifier = Modifier.height(24.dp))
//
//        Spacer(modifier = Modifier.height(16.dp))

//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            HorizontalDivider(modifier = Modifier.weight(1f))
//            Text("  Or  ", color = Color.Gray)
//            HorizontalDivider(modifier = Modifier.weight(1f))
//        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SingUpColors.White,
                unfocusedContainerColor = SingUpColors.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SingUpColors.White,
                unfocusedContainerColor = SingUpColors.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SingUpColors.White,
                unfocusedContainerColor = SingUpColors.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreeTerms,
                onCheckedChange = { agreeTerms = it }
            )
            Text(
                text = "I’m agree to The ",
                fontSize = 12.sp
            )
            Text(
                text = "Tarms",
                color = SingUpColors.DarkBlue,
                fontSize = 12.sp,
                modifier = Modifier.clickable { }
            )
            Text(
                text = " and ",
                fontSize = 12.sp
            )
            Text(
                text = "Privacy Policy",
                color = SingUpColors.DarkBlue,
                fontSize = 12.sp,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                creatAccount(context, name, email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SingUpColors.DarkBlue
            )
        ) {
            Text("Creat Account", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Do you have account? ", fontSize = 13.sp)
            Text(
                "Sign In",
                color = SingUpColors.DarkBlue,
                fontSize = 13.sp,
                modifier = Modifier.clickable { }
            )
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