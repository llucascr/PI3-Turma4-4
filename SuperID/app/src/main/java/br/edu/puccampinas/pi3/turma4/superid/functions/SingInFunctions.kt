package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

val auth = Firebase.auth

private fun singInAccount(context: Context, email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { taks ->
            if (taks.isSuccessful) {
                var user = auth.currentUser
                Log.d(TagsApp.AUTH_INFO.toString(), "singInWithEmailAndPassword: Success | uid: ${user?.uid}")
                onSuccess()
            } else {
                Log.e(TagsApp.AUTH_INFO.toString(), "singInWithEmailAndPassword: Failure | error: ${taks.exception}")
                Toast.makeText(context, "Authentication Failure", Toast.LENGTH_LONG).show()
            }
        }
        .addOnFailureListener { e ->
            Log.e(TagsApp.AUTH_INFO.toString(), "Failure SingIn: ${e.message}")
        }
}


fun validationSingIn(context: Context, email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    if (validationUtils.emailValidation(email)
        && validationUtils.emptyRegistrationFields(email, password)) {
        return
    }

    singInAccount(context, email, password, onSuccess, onFailure)
}