package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.util.Log
import android.widget.Toast
import br.edu.puccampinas.pi3.turma4.superid.screens.Category
import br.edu.puccampinas.pi3.turma4.superid.screens.PasswordItem
import br.edu.puccampinas.pi3.turma4.superid.screens.PasswordItemDetails
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private val db = Firebase.firestore
private val auth = Firebase.auth

fun getCategorys(onResult: (List<Category>) -> Unit) {
    val categoryList = mutableListOf<Category>()

    db.collection("users")
        .document(auth.uid ?: return)
        .collection("categorias")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val id = document.id
                val name = document.data["name"] as? String ?: "Sem nome"
                val quantidade = when (val q = document.data["quantidade"]) {
                    is Long -> q
                    is String -> q.toLongOrNull() ?: 0L
                    else -> 0L
                }
                val isDefault = document.data["isDefault"] as? Boolean ?: false

                categoryList.add(Category(id, name, quantidade, isDefault))

                Log.d("CATEGORY", "$id => ${document.data}")
            }
            onResult(categoryList)
        }
        .addOnFailureListener { exception ->
            Log.e("CATEGORY", "Erro ao buscar categorias", exception)
            onResult(emptyList())
        }
}

fun getCategoryById(categoryId: String, onResult: (String?) -> Unit) {
    val userId = auth.uid ?: run {
        onResult(null)
        return
    }

    db.collection("users")
        .document(userId)
        .collection("categorias")
        .document(categoryId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val name = document.getString("name") ?: "Sem nome"
                onResult(name)
            } else {
                onResult(null) // documento não existe
            }
        }
        .addOnFailureListener { exception ->
            Log.e("CATEGORY", "Erro ao buscar nome da categoria", exception)
            onResult(null)
        }
}

fun createCategory(
    context: Context,
    name: String,
    isDefault: Boolean = false,
    onResult: (Boolean) -> Unit
) {
    val categoryDoc = hashMapOf(
        "name" to name,
        "quantidade" to "0",
        "isDefault" to isDefault
    )

    checkEqualCategory(name) { isValid ->
        if (isValid) {
            db.collection("users").document(auth.uid.toString())
                .collection("categorias").document().set(categoryDoc)
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


fun getPasswordsByCategory(
    context: Context,
    categoryId: String,
    callback: (List<PasswordItem>) -> Unit
) {
    val passwordList = mutableListOf<PasswordItem>()

    db.collection("users")
        .document(auth.uid ?: return)
        .collection("categorias")
        .document(categoryId)
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

fun getPasswordDetails(
    context: Context,
    categoryId: String,
    documentId: String,
    callback: (PasswordItemDetails?) -> Unit
) {
    val db = Firebase.firestore

    db.collection("users")
        .document(auth.uid ?: return)
        .collection("categorias")
        .document(categoryId)
        .collection("senhas")
        .document(documentId)
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val passwordItemDetails = PasswordItemDetails(
                    title = document.getString("titulo") ?: "",
                    description = document.getString("descricao") ?: "",
                    login = document.getString("login") ?: "",
                    password = document.getString("senha") ?: "",
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
    categoryId: String,
    onResult: (Boolean) -> Unit
) {
    val userRef = db.collection("users").document(auth.uid ?: return)
    val passwordsRef = userRef.collection("categorias").document(categoryId).collection("senhas")

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
                    userRef.collection("categorias").document(categoryId)
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

fun editCategory(
    context: Context,
    categoryId: String,
    newName: String,
    onComplete: (Boolean) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val userId = auth.uid ?: return

    val categoryRef = db
        .collection("users")
        .document(userId)
        .collection("categorias")
        .document(categoryId)

    categoryRef.update("name", newName)
        .addOnSuccessListener {
            Toast.makeText(context, "Categoria atualizada com sucesso", Toast.LENGTH_SHORT).show()
            onComplete(true)
        }
        .addOnFailureListener {
            Toast.makeText(context, "Erro ao atualizar a categoria", Toast.LENGTH_SHORT).show()
            onComplete(false)
        }
}