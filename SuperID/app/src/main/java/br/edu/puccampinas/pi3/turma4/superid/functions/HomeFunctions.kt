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

fun getCategorys(onResult: (List<Pair<String, Long>>) -> Unit) {
    val categoryList = mutableListOf<Pair<String, Long>>()

    db.collection("users")
        .document(auth.uid ?: return)
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

fun createCategory(context: Context, name: String) {
    val categoryDoc = hashMapOf<String, String>(
        "name" to name,
        "quantidade" to "0"
    )

    checkEqualCategory(name) { isValid ->
        if (isValid) {
            db.collection("users").document(auth.uid.toString())
                .collection("categorias").document(name).set(categoryDoc)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("FIRESTORE-INFO", "Categoria Criada!")
                        Toast.makeText(context, "Categoria criada com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("FIRESTORE-INFO", "Erro ao criar a categoria: ${task.exception}")
                    }
                }
        } else {
            Toast.makeText(context, "Já existe uma categoria com esse nome", Toast.LENGTH_LONG).show()
        }
    }
}

fun checkEqualCategory(nameCategory: String, callback: (Boolean) -> Unit) {
    db.collection("users")
        .document(auth.uid.toString())
        .collection("categorias")
        .get()
        .addOnSuccessListener { result ->
            var notIsEquals = true
            for (document in result) {
                val name = document.data["name"] as? String ?: "Sem nome"
                if (nameCategory == name) {
                    notIsEquals = false
                    break
                }
            }
            callback(notIsEquals)
        }
        .addOnFailureListener { exception ->
            Log.e("CATEGORY", "Erro ao buscar categorias", exception)
            callback(false) // Em caso de erro, assume que não pode criar
        }
}