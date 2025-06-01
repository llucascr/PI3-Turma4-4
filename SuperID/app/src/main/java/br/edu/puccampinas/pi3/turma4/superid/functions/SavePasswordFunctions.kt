package br.edu.puccampinas.pi3.turma4.superid.functions

import android.os.Build
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Base64
import androidx.annotation.RequiresApi


private val auth = Firebase.auth
private val db = Firebase.firestore


//função de verificação de inputs vazios
@RequiresApi(Build.VERSION_CODES.O)
fun verifyInputs(titulo: String, descricao: String?, login: String, senha: String, url: String, categoryId: String): Boolean{
    if(titulo.isEmpty() || login.isEmpty() || senha.isEmpty()){
        Log.e("FIREBASE", "Campos vazios!")
        return false
    }else{
        saveNewPw(titulo,descricao,login,senha,url, categoryId)
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
@RequiresApi(Build.VERSION_CODES.O)
private fun saveNewPw(titulo: String, descricao: String?, login: String, senha: String,
                      url: String, categoryId: String){
    val userId = auth.currentUser
    val accessToken = toBase64(getStringToken())
    val senhacriptografada = encrypt(userId.toString(), senha)
    Log.i("user id", "User: $userId")
    val pwInformations = hashMapOf(
        "titulo" to titulo,
        "descricao" to descricao,
        "url" to url,
        "login" to login,
        "senha" to senhacriptografada,
        "accessToken" to accessToken
    )
    db.collection("users").document("${userId?.uid}")
        .collection("categorias").document(categoryId)
        .collection("senhas").document().set(pwInformations)
        .addOnCompleteListener{taks ->
            if (taks.isSuccessful) {
                Log.i("FIRESTORE-INFO", "Password saved!")
                val teste = decrypt(userId.toString(), senhacriptografada)
                Log.i("SENHA", teste)
            } else {
                Log.e("FIRESTORE-INFO", "Error saving password: ${taks.exception}")
            }
        }
        .addOnFailureListener { error ->
            Log.e("FIRESTORE-INFO", "Error on saving password: ", error)
        }
}
