package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

private val auth = Firebase.auth

private fun signInAccount(context: Context, email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { taks ->
            if (taks.isSuccessful) {
                var user = auth.currentUser
                Log.d("AUTH-INFO", "singInWithEmailAndPassword: Success | uid: ${user?.uid}")
                validationUtils.saveEmailForAuthentication(context, email)
                onSuccess()
            } else {
                Log.e("AUTH-INFO", "singInWithEmailAndPassword: Failure | error: ${taks.exception}")
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(
                context,
                "Email ou senha invalidos",
                Toast.LENGTH_LONG,
            ).show()
            Log.e("AUTH-INFO", "Failure SingIn: ${e.message}")
        }
}


fun validationSignIn(context: Context, email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    if (validationUtils.emailValidation(email)
        && validationUtils.emptyRegistrationFields(email, password)) {
        return
    } else if (validationUtils.checkUserAuth(context)) { // Faz login se o email do usuário estiver salvo nas SharedPreferences
        validationUtils.reauthenticateUser(
            context,
            password,
            onSuccess,
            onFailure
        )
        return
    }

    signInAccount(context, email, password, onSuccess, onFailure)
}