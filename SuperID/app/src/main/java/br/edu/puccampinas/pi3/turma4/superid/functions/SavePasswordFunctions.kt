package br.edu.puccampinas.pi3.turma4.superid.functions

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log


private val auth = Firebase.auth
private val db = Firebase.firestore
//TODO: VER COMO MOSTRAR MENSAGENS DE ERRO E SUCESSO
// TODO: CONECTAR A FUNÇÃO VERIFYINPUTS COM A SCREEN SAVE NEW PASSWORD

//função de verificação de inputs vazios
fun verifyInputs(titulo: String,descricao: String?, login: String, senha: String): Boolean{
    if(titulo.isEmpty() || login.isEmpty() || senha.isEmpty()){
        Log.e("FIREBASE", "Campos vazios!")
        return false
    }else{
        SaveNewPw(titulo,descricao,login,senha)
        return true
    }


}
//função para salvar senha no firestore
fun SaveNewPw(titulo: String,descricao: String?,login: String,senha: String){
    val userId = auth.currentUser
    Log.i("user id", "User: ${userId}")
    val PwInformations = hashMapOf<String,String?>(
        "titulo" to titulo,
        "descricao" to descricao,
        "login" to login,
        "senha" to senha,
        "accessToken" to " "
    )
    db.collection("users").document("${userId?.uid}")
        .collection("categorias").document("Sites Web")
        .collection("senhas").document().set(PwInformations)
        .addOnCompleteListener{taks ->
            if (taks.isSuccessful) {
                Log.i("FIRESTORE-INFO", "Password saved!")
            } else {
                Log.e("FIRESTORE-INFO", "Error saving user: ${taks.exception}")
            }
        }
        .addOnFailureListener { error ->
            Log.e("FIRESTORE-INFO", "Error on saving password: ", error)
        }
}
