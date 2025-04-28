package br.edu.puccampinas.pi3.turma4.superid.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SingInColors.backgroundColor
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme

@Composable
fun TermsOfUseScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SingInColors.backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 30.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Botão Voltar
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = SingInColors.primaryGreen),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "< Voltar",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Título
            Text(
                text = "Termos de Uso",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = SingInColors.primaryGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Corpo dos Termos
            TermsSection(
                title = "Uso do Aplicativo",
                description = "Ao utilizar o SuperID, você concorda com estes termos e com o uso de seus dados conforme descrito."
            )
            TermsSection(
                title = "Cadastro e Segurança",
                description = "O usuário deve criar uma conta com nome, e-mail e senha mestre. É responsabilidade do usuário manter essa senha em segurança."
            )
            TermsSection(
                title = "Armazenamento de Dados",
                description = "As senhas e informações são criptografadas e armazenadas de forma segura no Firebase. Os dados são utilizados apenas para funcionamento do app."
            )
            TermsSection(
                title = "Login Sem Senha",
                description = "O SuperID permite login em sites parceiros via QR Code, sem expor senhas. O uso dessa função implica concordância com esse processo."
            )
            TermsSection(
                title = "Limitação de Responsabilidade",
                description = "Este aplicativo é educacional e não oferece garantias de segurança profissional ou corporativa. O uso é de responsabilidade do usuário."
            )
            TermsSection(
                title = "Atualizações",
                description = "Os termos podem ser atualizados a qualquer momento. O uso contínuo do app confirma a aceitação das novas regras."
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TermsSection(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = SingInColors.primaryGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            color = SingInColors.textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsOfuserScreenPreview() {
    SuperIDTheme {
        TermsOfUseScreen(navController = rememberNavController())
    }
}
