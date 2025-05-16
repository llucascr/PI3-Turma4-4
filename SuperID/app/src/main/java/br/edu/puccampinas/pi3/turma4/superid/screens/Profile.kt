package br.edu.puccampinas.pi3.turma4.superid.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.functions.getSavedName
import br.edu.puccampinas.pi3.turma4.superid.functions.reloadEmailVerification
import br.edu.puccampinas.pi3.turma4.superid.functions.resetPassword
import br.edu.puccampinas.pi3.turma4.superid.functions.sendEmailVerification
import br.edu.puccampinas.pi3.turma4.superid.functions.validationUtils
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    var name = getSavedName(context).toString()
    var email = validationUtils.getSavedEmail(context).toString()

    var emailVerification = reloadEmailVerification()
//    var emailVerification = false

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
        ) {
            Text(
                text = "Perfil",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .padding(24.dp),
                    tint = Color.DarkGray
                )
                // Inplementar futuramente: Botão de add foto de Perfil
//                Box(
//                    modifier = Modifier
//                        .size(24.dp)
//                        .align(Alignment.BottomEnd)
//                        .background(DarkColors.primaryGreen, CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.CameraAlt,
//                        contentDescription = "Icone camera perfil",
//                        modifier = Modifier.size(15.dp)
//                    )
//                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileField(label = "Nome", value = name)
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            ProfileField(label = "Email", value = email)
            Spacer(modifier = Modifier.padding(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!emailVerification) {
                    Text(
                        text = "Verifique seu Email",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = "Email verificado",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(
                    onClick = { sendEmailVerification() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Reenviar email de verificação", fontSize = 12.sp)
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            ProfileField(
                label = "Senha",
                value = "",
                trailing = {
                    Button(
                        onClick = { resetPassword(email, context) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Redefinir senha", fontSize = 12.sp)
                    }
                }
            )
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { validationUtils.logoutUser(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Sair", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    trailing: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp
            )
            if (value.isNotBlank()) {
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp
                )
            }
            trailing?.invoke()
        }
    }
}

@Preview
@Composable
fun PreviewProfile() {
    SuperIDTheme(darkTheme = true, dynamicColor = false) {
        ProfileScreen(navController = rememberNavController())
    }
}