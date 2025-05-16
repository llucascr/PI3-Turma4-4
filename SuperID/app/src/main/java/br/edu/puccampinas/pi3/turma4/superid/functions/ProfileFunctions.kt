package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth


fun resetPassword(email: String, context: Context) {
    var auth = Firebase.auth
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.i("AUTH-FIREBASE", "Email de redefinição de senha enviado com suscesso")
                Toast.makeText(
                    context,
                    "Email de redefinição de senha enviado",
                    Toast.LENGTH_LONG,
                ).show()
            } else {
                Log.e("AUTH-FIREBASE", "Erro ao enviar email de redefinição de senha")
                Toast.makeText(
                    context,
                    "Não foi possivel enviar o email de redefinição de senha",
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
}

/**
 * Salva o email do usuário para ele se reautenticar ao entrar novamente no app
 */
fun saveName(context: Context, name: String) {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    prefs.edit() { putString("user_name", name) }
}

/**
 * Retorna o email salvo para fazer a reautenticação
 */
fun getSavedName(context: Context): String? {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return prefs.getString("user_name", null)
}

fun sendEmailVerification () {
    val user = com.google.firebase.ktx.Firebase.auth.currentUser

    user?.sendEmailVerification()
        ?.addOnCompleteListener {taks ->
            if (taks.isSuccessful) {
                Log.d("AUTH-EMAIL", "E-mail sent successfully: ${user.email}")

            } else {
                Log.e("AUTH-EMAIL", "Error sending validation email: ${taks.exception}")
            }
        }
}

/**
 * Verifica se o email foi verificado ou não
 */
fun reloadEmailVerification(): Boolean {
    val user = FirebaseAuth.getInstance().currentUser
    var verification: Boolean = false

    user?.reload()?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            if (user.isEmailVerified) {
                Log.d("EMAIL",  "Email verificado com sucesso!")
                verification = true
            }
            else {
                Log.d("EMAIL", "Email não verificado")
            }
        }
    }
    return verification
}
