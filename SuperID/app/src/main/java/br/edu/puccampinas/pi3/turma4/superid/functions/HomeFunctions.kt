package br.edu.puccampinas.pi3.turma4.superid.functions

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun getCategorys(onResult: (List<Pair<String, Long>>) -> Unit) {
    val db = Firebase.firestore
    val uid = Firebase.auth.uid
    val categoryList = mutableListOf<Pair<String, Long>>()

    db.collection("users")
        .document(uid ?: return)
        .collection("categorias")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val name = document.data["name"] as? String ?: "Sem nome"
                val quantidade = document.data["quantidade"] as? Long ?: 0L
                categoryList.add(name to quantidade)
                Log.d("CATEGORY", "${document.id} => ${document.data}")
            }
            onResult(categoryList)
        }
        .addOnFailureListener { exception ->
            Log.e("CATEGORY", "Erro ao buscar categorias", exception)
            onResult(emptyList())
        }
}

fun createCategory(name: String) {
    val db = Firebase.firestore
    val auth = Firebase.auth

    var categoryDoc = hashMapOf<String, String>(
        "name" to name,
        "quantidae" to "0"
    )

    db.collection("users").document("${auth.uid}")
        .collection("categorias").document(name).set(categoryDoc)
        .addOnCompleteListener{taks ->
            if (taks.isSuccessful) {
                Log.i("FIRESTORE-INFO", "Categoria Criada!")
            } else {
                Log.e("FIRESTORE-INFO", "Erro ao criar a categoria: ${taks.exception}")
            }
        }
        .addOnFailureListener { error ->
            Log.e("FIRESTORE-INFO", "Erro ao criar a categoria: ", error)
        }
}