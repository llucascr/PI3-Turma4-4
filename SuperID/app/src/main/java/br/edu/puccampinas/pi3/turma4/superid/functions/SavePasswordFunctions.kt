package br.edu.puccampinas.pi3.turma4.superid.functions

import android.os.Build
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Base64
import kotlin.math.log


private val auth = Firebase.auth
private val db = Firebase.firestore


//função de verificação de inputs vazios
fun verifyInputs(titulo: String,descricao: String?, login: String, senha: String, url: String, categoryName: String): Boolean{
    if(titulo.isEmpty() || login.isEmpty() || senha.isEmpty()){
        Log.e("FIREBASE", "Campos vazios!")
        return false
    }else{
        SaveNewPw(titulo,descricao,login,senha,url, categoryName)
        return true
    }


}
private fun toBase64(string: String): String {
    return Base64.encodeToString(string.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
}

//Gera token de 256 caracteres
private fun getStringToken():String{
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
    var string: String = ""
    var char: Char
    for(i in 0..255){
        char = characters.random()
        string = string + char
    }
    return string
}
//função para salvar senha no firestore
private fun SaveNewPw(titulo: String,descricao: String?,login: String,senha: String,
                      url: String, categoryName: String){
    val userId = auth.currentUser
    val accessToken = toBase64(getStringToken())
    val senhacriptografada = encrypt(senha)
    Log.i("user id", "User: ${userId}")
    val PwInformations = hashMapOf<String,String?>(
        "titulo" to titulo,
        "descricao" to descricao,
        "url" to url,
        "login" to login,
        "senha" to senhacriptografada,
        "accessToken" to accessToken
    )
    db.collection("users").document("${userId?.uid}")
        .collection("categorias").document(categoryName)
        .collection("senhas").document().set(PwInformations)
        .addOnCompleteListener{taks ->
            if (taks.isSuccessful) {
                Log.i("FIRESTORE-INFO", "Password saved!")
            } else {
                Log.e("FIRESTORE-INFO", "Error saving password: ${taks.exception}")
            }
        }
        .addOnFailureListener { error ->
            Log.e("FIRESTORE-INFO", "Error on saving password: ", error)
        }
}
