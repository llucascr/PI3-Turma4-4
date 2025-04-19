package br.edu.puccampinas.pi3.turma4.superid.functions

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private fun creatAccount(context: Context, name: String, email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {taks ->
            if (taks.isSuccessful) {
                Log.d("AUTH-INFO","createUserWithEmail:success | UID: ${taks.result.user!!.uid}")
                saveAccount(context, name, taks.result.user!!, onSuccess, onFailure)
                sendEmailVerification()
            } else {
                Log.w("AUTH-INFO", "createUserWithEmail:failure", taks.exception)
            }
        }
}

@SuppressLint("HardwareIds")
private fun saveAccount(context: Context, name: String, user: FirebaseUser, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val db = Firebase.firestore

    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    val userDoc: HashMap<String, String> = hashMapOf(
        "IMEI" to androidId,
        "UID" to user.uid,
        "NAME" to name
    )

    db.collection("users").document(user.uid).set(userDoc)
        .addOnCompleteListener {taks ->
            if (taks.isSuccessful) {
                Log.d("FIRESTORE-INFO", "User saved!")
                onSuccess()
            } else {
                Log.w("FIRESTORE-INFO", "Error saving user: ${taks.exception}")
            }
        }
        .addOnFailureListener {e ->
            Log.e("FIRESTORE-INFO", "Error in save function: ", e)
            onFailure(e)
        }
}

private fun sendEmailVerification () {
    val user = Firebase.auth.currentUser

    user?.sendEmailVerification()
        ?.addOnCompleteListener {taks ->
            if (taks.isSuccessful) {
                Log.d("AUTH-EMAIL", "E-mail sent successfully: ${user.email}")
            } else {
                Log.e("AUTH-EMAIL", "Error sending validation email: ${taks.exception}")
            }
        }
}

private fun emailValidation() {
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


/**
 * Chama as funções de validação dos campos do cadastro,
 * para descobrir se todos são validos
 */
private fun fieldValidation(name: String, email: String, password: String): Boolean {
    if (validationUtils.emptyRegistrationFields(name, email, password)
        && validationUtils.emailValidation(email)
        && validationUtils.passwordInvalid(password)) {
            return false
    }

    return true
}

fun validationSingUp(context: Context, name: String, email: String, password: String,
                     onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    if (!fieldValidation(name, email, password)) {
        onFailure(Exception("Campos inválidos"))
        return
    }

    creatAccount(context, name, email, password, onSuccess, onFailure)
}