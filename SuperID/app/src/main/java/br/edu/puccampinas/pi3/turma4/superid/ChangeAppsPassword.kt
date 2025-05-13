package com.example.alterarsenha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.puccampinas.pi3.turma4.superid.R

class ChangeAppsPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlterarSenha()
        }
    }
}

@Composable
fun HeaderAlterarSenhas() {
    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .fillMaxHeight(0.1f),
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            onClick = { /* ação de voltar */ },
            modifier = Modifier.height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
          Image(painter = painterResource(R.drawable.iconqrcode), contentDescription = "Sofia")
        }
    }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .height(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text("Alterar Senha", color = Color.White, fontSize = 30.sp)
    }
}

@Composable
fun TextFildsAlterarSenhas() {
    val verde = Color(0xFF166534)
    val branco = Color(0xFFFFFFFF)
    val preto = Color(0xFF000000)

    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var nova_senha by remember { mutableStateOf("") }
    var conf_nova_senha by remember { mutableStateOf("") }

    val commonModifier = Modifier
        .fillMaxWidth()
        .padding(top = 13.dp)
        .height(50.dp)

    OutlinedTextField(
        value = titulo,
        onValueChange = { titulo = it },
        placeholder = { Text("Titulo", color = Color.Gray) },
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
        shape = RoundedCornerShape(15.dp)
    )

    OutlinedTextField(
        value = descricao,
        onValueChange = { descricao = it },
        placeholder = { Text("Descrição", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = verde,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier.height(70.dp),
        shape = RoundedCornerShape(15.dp)
    )

    OutlinedTextField(
        value = login,
        onValueChange = { login = it },
        placeholder = { Text("Login", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = verde,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier,
        shape = RoundedCornerShape(15.dp)
    )

    OutlinedTextField(
        value = nova_senha,
        onValueChange = { nova_senha = it },
        placeholder = { Text("Nova Senha", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = verde,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier,
        shape = RoundedCornerShape(15.dp)
    )

    OutlinedTextField(
        value = conf_nova_senha,
        onValueChange = { conf_nova_senha = it },
        placeholder = { Text("Confirmar Senha", color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = verde,
            focusedBorderColor = verde,
            focusedTextColor = preto,
            unfocusedTextColor = preto,
            cursorColor = verde,
            unfocusedContainerColor = branco,
            focusedContainerColor = branco,
        ),
        modifier = commonModifier,
        shape = RoundedCornerShape(15.dp)
    )

    Box(
        modifier = Modifier
            .padding(top = 27.dp)
            .fillMaxWidth()
            .height(50.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(15.dp),
                ambientColor = Color(0xFFB0FFB0),
                spotColor = Color(0xFFB0FFB0)
            )
            .background(
                color = Color(0xff0f6630),
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        Button(
            onClick = { /* ação de salvar */ },
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(15.dp),
            contentPadding = PaddingValues()
        ) {
            Text("Salvar", color = Color.White)
        }
    }
}

@Composable
fun BottomBar() {
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .height(60.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                drawLine(
                    color = Color.White,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.iconperson),
            contentDescription = "Imagem de casa",
            modifier = Modifier.size(30.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.iconqrcode),
            contentDescription = "Imagem de qr code",
            modifier = Modifier.size(100.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.iconhome),
            contentDescription = "Imagem de perfil",
            modifier = Modifier.size(30.dp)
        )
    }
}

@Preview
@Composable
fun AlterarSenha() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        horizontalAlignment = Alignment.Start
    ) {
        HeaderAlterarSenhas()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(20.dp)
                .background(color = Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextFildsAlterarSenhas()
        }

        BottomBar()
    }
}