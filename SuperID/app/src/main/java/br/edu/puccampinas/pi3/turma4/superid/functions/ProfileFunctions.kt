package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine


fun resetPassword(email: String, isVerificated: Boolean ,context: Context) {
    var auth = Firebase.auth

    if (isVerificated) {
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
    } else {
        Toast.makeText(
            context,
            "Verifique o email para redefinir a senha",
            Toast.LENGTH_LONG,
        ).show()
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
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun reloadEmailVerification(): Boolean {
    val user = FirebaseAuth.getInstance().currentUser ?: return false

    return suspendCancellableCoroutine { continuation ->
        user.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val verified = user.isEmailVerified
                Log.d("EMAIL", if (verified) "Email verificado com sucesso!" else "Email não verificado")
                continuation.resume(verified, onCancellation = null)
            } else {
                continuation.resume(false, onCancellation = null)
            }
        }
    }

}
