package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.edu.puccampinas.pi3.turma4.superid.screens.PasswordItem
import br.edu.puccampinas.pi3.turma4.superid.screens.PasswordItemDetails
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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
                val name = document.data["nome"] as? String ?: "Sem nome"
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

fun createCategory(
    context: Context,
    name: String,
    onResult: (Boolean) -> Unit
) {
    val categoryDoc = hashMapOf<String, String>(
        "nome" to name,
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
                        onResult(true)
                    } else {
                        Log.e("FIRESTORE-INFO", "Erro ao criar a categoria: ${task.exception}")
                        onResult(false)
                    }
                }
        } else {
            Toast.makeText(context, "Já existe uma categoria com esse nome", Toast.LENGTH_LONG).show()
            onResult(false)
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
                val name = document.data["nome"] as? String ?: "Sem nome"
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


fun getPasswordsByCategory(
    context: Context,
    categoryName: String,
    callback: (List<PasswordItem>) -> Unit
) {
    val passwordList = mutableListOf<PasswordItem>()

    db.collection("users")
        .document(auth.uid ?: return)
        .collection("categorias")
        .document(categoryName)
        .collection("senhas")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val documentId = document.id
                val titulo = document.getString("titulo") ?: "Sem título"
                val descricao = document.getString("descricao") ?: "Sem descrição"
                val passwordItem = PasswordItem(documentId,titulo, descricao)
                passwordList.add(passwordItem)

                Log.d("PASSWORD", "${document.id} => ${document.data}")
            }
            callback(passwordList)
        }
        .addOnFailureListener { e ->
            Log.e("PASSWORD", "Erro ao buscar senhas", e)
            Toast.makeText(context, "Erro ao buscar senhas", Toast.LENGTH_SHORT).show()
            callback(emptyList())
        }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getPasswordDetails(
    context: Context,
    categoryName: String,
    documentId: String,
    callback: (PasswordItemDetails?) -> Unit
) {
    val db = Firebase.firestore

    db.collection("users")
        .document(auth.uid ?: return)
        .collection("categorias")
        .document(categoryName)
        .collection("senhas")
        .document(documentId)
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val passwordItemDetails = PasswordItemDetails(
                    title = document.getString("titulo") ?: "",
                    description = document.getString("descricao") ?: "",
                    login = document.getString("login") ?: "",
                    password = decrypt(auth.uid.toString(),
                        document.getString("senha").toString(),document.getString("salt").toString(),
                        document.getString("iv").toString()) ?: "",
                    url = document.getString("url") ?: ""
                )

                Log.d("PASSWORD_DETAILS", "${document.id} => ${document.data}")
                callback(passwordItemDetails)
            } else {
                Log.d("PASSWORD_DETAILS", "Nenhum documento encontrado")
                callback(null)
            }
        }
        .addOnFailureListener { e ->
            Log.e("PASSWORD_DETAILS", "Erro ao buscar detalhes da senha", e)
            Toast.makeText(context, "Erro ao buscar detalhes da senha", Toast.LENGTH_SHORT).show()
            callback(null)
        }
}

fun deleteCategory(
    context: Context,
    categoryName: String,
    onResult: (Boolean) -> Unit
) {
    val userRef = db.collection("users").document(auth.uid ?: return)
    val passwordsRef = userRef.collection("categorias").document(categoryName).collection("senhas")

    // 1. Buscar todas as senhas da categoria
    passwordsRef.get()
        .addOnSuccessListener { result ->
            val batch = db.batch()

            // 2. Adicionar cada exclusão no batch
            for (document in result) {
                batch.delete(document.reference)
            }

            // 3. Executar exclusão das senhas
            batch.commit()
                .addOnSuccessListener {
                    // 4. Depois deletar a categoria
                    userRef.collection("categorias").document(categoryName)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("CATEGORY_DELETE", "Categoria e senhas deletadas com sucesso.")
                            Toast.makeText(context, "Categoria deletada com sucesso!", Toast.LENGTH_SHORT).show()
                            onResult(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("CATEGORY_DELETE", "Erro ao deletar categoria", e)
                            Toast.makeText(context, "Erro ao deletar categoria", Toast.LENGTH_SHORT).show()
                            onResult(false)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("CATEGORY_DELETE", "Erro ao deletar senhas da categoria", e)
                    Toast.makeText(context, "Erro ao deletar senhas da categoria", Toast.LENGTH_SHORT).show()
                    onResult(false)
                }
        }
        .addOnFailureListener { e ->
            Log.e("CATEGORY_DELETE", "Erro ao buscar senhas da categoria", e)
            Toast.makeText(context, "Erro ao buscar senhas da categoria", Toast.LENGTH_SHORT).show()
            onResult(false)
        }
}