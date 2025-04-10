package br.edu.puccampinas.pi3.turma4.superid.functions

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

public fun creatAccount(context: Context, nome: String, email: String, senha: String,
                        onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
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

//fun validationFildsSingUp(nome: String, email: String, senha: String) {
//    if (areSingUp)
//}

fun validationSingUp(context: Context, nome: String, email: String, senha: String,
                     onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//    if (!validationFildsSingUp(nome, email, senha)) {
//
//    }

}