package br.edu.puccampinas.pi3.turma4.superid.functions

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

public fun creatAccount(context: Context, name: String, email: String, password: String) {
    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {taks ->
            if (taks.isSuccessful) {
                Log.d("AUTH-INFO",
                    "createUserWithEmail:success | UID: ${taks.result.user!!.uid}")
                saveAccount(context, name, taks.result.user!!)
                sendEmailVerification()
            } else {
                Log.w("AUTH-INFO", "createUserWithEmail:failure", taks.exception)
            }
        }
}

@SuppressLint("HardwareIds")
private fun saveAccount(context: Context, name: String, user: FirebaseUser) {
    val db = Firebase.firestore

    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    val userDoc = hashMapOf(
        "name" to name,
        "email" to user.email,
        "uid" to user.uid,
        "ime" to androidId,
        "emailValidated" to false,
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

fun sendEmailVerification () {
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore

    user?.sendEmailVerification()
        ?.addOnCompleteListener {taks ->
            if (taks.isSuccessful) {
                Log.d("AUTH-EMAIL", "E-mail sent successfully: ${user.email}")
            } else {
                Log.e("AUTH-EMAIL", "Error sending validation email: ${taks.exception}")
            }
        }
}

fun emailValidation() {
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore

    user?.reload()
        ?.addOnCompleteListener {task ->
            if (task.isSuccessful) {
                if (user.isEmailVerified) {
                    db.collection("UserDocs").document(user.uid)
                        .update("emailvalidated", true)
                        .addOnCompleteListener {
                            Log.d("AUTH-EMAIL", "Email validado com sucesso")
                        }
                        .addOnFailureListener { e ->
                            Log.e("AUTH-EMAIL","ERRO ao validar o Email: $e")
                        }
                }else {
                    Log.d("AUTH-EMAIL", "Email ainda não verificado")
                }
            } else {
                Log.e("AUTH-EMAIL", "Erro ao recarregar usuário: ${task.exception}")
            }
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