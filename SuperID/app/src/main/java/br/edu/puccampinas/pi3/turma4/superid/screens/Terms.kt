package br.edu.puccampinas.pi3.turma4.superid.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun TermsOfUseScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            // Botão Voltar
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text("< Voltar", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "Termos de Uso",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texto dos termos
            Text(
                text = """
                -Uso do Aplicativo
                Ao utilizar o SuperID, você concorda com estes termos e com o uso de seus dados conforme descrito.

                -Cadastro e Segurança
                O usuário deve criar uma conta com nome, e-mail e senha mestre. É responsabilidade do usuário manter essa senha em segurança.

                -Armazenamento de Dados
                As senhas e informações são criptografadas e armazenadas de forma segura no Firebase. Os dados são utilizados apenas para funcionamento do app.

                -Login Sem Senha
                O SuperID permite login em sites parceiros via QR Code, sem expor senhas. O uso dessa função implica concordância com esse processo.

                -Limitação de Responsabilidade
                Este aplicativo é educacional e não oferece garantias de segurança profissional ou corporativa. O uso é de responsabilidade do usuário.

                -Atualizações
                Os termos podem ser atualizados a qualquer momento. O uso contínuo do app confirma a aceitação das novas regras.
                """.trimIndent(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsOfuserScreenPreview() {
    SuperIDTheme(darkTheme = false, dynamicColor = false) {
        TermsOfUseScreen(navController = rememberNavController())
    }
}
