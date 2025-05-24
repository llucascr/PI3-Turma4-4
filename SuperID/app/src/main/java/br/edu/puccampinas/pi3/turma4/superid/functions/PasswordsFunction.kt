package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow

private val db = Firebase.firestore
private val auth = Firebase.auth

fun getPasswords(categoria: String, onResult: (List<Pair<String, String>>) -> Unit) {
    val categoryList = mutableListOf<Pair<String, String>>()

    db.collection("users")
        .document(auth.uid ?: return)
        .collection("categorias")
        .document(categoria)
        .collection("senhas")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val titulo = document.data["titulo"] as? String ?: "null"
                val url = document.data["url"] as? String ?: titulo
                categoryList.add(titulo to url)
                Log.d("CATEGORY", "${document.id} => ${document.data}")
            }
            onResult(categoryList)
        }
        .addOnFailureListener { exception ->
            Log.e("CATEGORY", "Erro ao buscar categorias", exception)
            onResult(emptyList())
        }
}
