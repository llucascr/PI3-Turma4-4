package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


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
