package br.edu.puccampinas.pi3.turma4.superid

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SingUpActivity : ComponentActivity() {

//    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

//        auth = Firebase.auth
//        db = Firebase.firestore

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperIDTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
////            reload()
//        }
    }

    fun creatAccount(context: Context, nome: String, email: String, senha: String) {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener {taks ->
                if (taks.isSuccessful) {
                    Log.d("AUTH-INFO",
                        "createUserWithEmail:success | UID: ${taks.result.user!!.uid}")
                    saveAccount(context, nome, taks.result.user!!)
                } else {
                    Log.w("AUTH-INFO", "createUserWithEmail:failure", taks.exception)
                }
            }
    }

    @SuppressLint("HardwareIds")
    private fun saveAccount(context: Context, nome: String, user: FirebaseUser) {
        val db = Firebase.firestore

        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val userDoc = hashMapOf(
            "nome" to nome,
            "email" to user.email,
            "uid" to user.uid,
            "ime" to androidId,
            "validarEmail" to false,
        )

        db.collection("UserDocs").add(userDoc)
            .addOnCompleteListener {taks ->
                if (taks.isSuccessful) {
                    Log.d("FIRESTORE-INFO", "User saved!")
                } else {
                    Log.w("FIRESTORE-INFO", "Error saving user: ${taks.exception}")
                }
            }
            .addOnFailureListener {e ->
                Log.e("FIRESTORE-INFO", "Error in save function: ", e)
            }
    }
}

@Composable
fun Greeting2(modifier: Modifier = Modifier) {

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val context = LocalContext.current

    val singUpActivity = SingUpActivity()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = nome,
            onValueChange = {nome = it},
            label = { Text("Nome: ") }
        )

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Email: ") }
        )

        OutlinedTextField(
            value = senha,
            onValueChange = {senha = it},
            label = { Text("Senha: ") }
        )

        Button(
            onClick = {
                singUpActivity.creatAccount(context, nome, email, senha)
            }
        ) {
            Text("Cadastrar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SuperIDTheme {
        Greeting2()
    }
}