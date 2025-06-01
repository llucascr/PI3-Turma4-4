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
                validationUtils.saveEmailForAuthentication(context, email)
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
                createCategory(context, "Aplicativos", isDefault = true) { success ->
                    Log.d("CATEGORY", "Categoria Aplicativos criada: $success")
                }

                createCategory(context, "Sites Web", isDefault = true) { success ->
                    Log.d("CATEGORY", "Categoria Sites Web criada: $success")
                }

                createCategory(context, "Teclado de Acesso Físico", isDefault = true) { success ->
                    Log.d("CATEGORY", "Categoria Teclado de Acesso Físico criada: $success")
                }
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



fun validationSingUp(context: Context, name: String, email: String, password: String,
                     onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    if (!validationUtils.fieldValidation(name, email, password)) {
        onFailure(Exception("Campos inválidos"))
        return
    }

    creatAccount(context, name, email, password, onSuccess, onFailure)
}