package br.edu.puccampinas.pi3.turma4.superid.functions

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun getCategorys(onResult: (List<Pair<String, String>>) -> Unit) {
    val db = Firebase.firestore
    val auth = Firebase.auth

    db.collection("users").document("${auth.uid}")
        .collection("categorias").get()
        .addOnSuccessListener { result ->
            val list = result.documents.mapNotNull { doc ->
                val name = doc.getString("nome") ?: return@mapNotNull null
                val count = doc.getLong("quantidade")?.toString() ?: "0"
                name to count
            }
            onResult(list)
        }
        .addOnFailureListener { exception ->
            Log.d("CATEGORY", "get failed with ", exception)
            onResult(emptyList())
        }
}