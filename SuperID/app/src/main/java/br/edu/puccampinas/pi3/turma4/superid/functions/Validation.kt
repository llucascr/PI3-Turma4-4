package br.edu.puccampinas.pi3.turma4.superid.functions

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.edit
import br.edu.puccampinas.pi3.turma4.superid.WelcomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth

object validationUtils  {

    /**
     * Verifica por meio de regex se o formato do email inserido é valido
     */
    fun emailValidation(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        if (!emailRegex.matches(email)) {
            Log.i("FIREBASE-VALIDATION", "EMAIL INVALIDO")
            return true
        }
        return false
    }

    /**
     * Verifica se a senha está de acordo com as regras definidas
     */
    fun passwordInvalid(password: String): Boolean {
        if (password.length < 6) {
            Log.i("FIREBASE-VALIDATION", "SENHA INVALIDA")
            return true
        }
        return false
    }

    /**
     * Verifica se os campos de dados estão vazios
     */
    internal fun emptyRegistrationFields(name: String, email: String, password: String): Boolean {
        if (email == "" || password == "" || name == "") {
            Log.i("FIREBASE", "CAMPOS DE DADOS ENTÃO VAZIOS")
            return true
        }
        return false
    }

    /**
     * Verifica se os campos de dados estão vazios
     */
    internal fun emptyRegistrationFields(email: String, password: String): Boolean {
        if (email == "" || password == "") {
            Log.i("FIREBASE", "CAMPOS DE DADOS ENTÃO VAZIOS")
            return true
        }
        return false
    }

    /**
     * Salva o email do usuário para ele se reautenticar ao entrar novamente no app
     */
    fun saveEmailForAuthentication(context: Context, email: String) {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit() { putString("user_email", email) }
    }

    /**
     * Retorna o email salvo para fazer a reautenticação
     */
    fun getSavedEmail(context: Context): String? {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getString("user_email", null)
    }


    /**
     * Reautentica o usuário com email salvo locamente e a senha fornecida
     */
    fun reauthenticateUser(
        context: Context,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = Firebase.auth.currentUser
        var email = getSavedEmail(context)

        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential)
                .addOnCompleteListener {
                    Log.i("LOGIN", "Reautenticado com sucesso")
                    onSuccess()
                }
        } else {
            onFailure(Exception("Usuário não autenticado ou e-mail não salvo"))
        }
    }

    /**
     * Faz o logout da autenticação do firebase e retira o email salvo localmente
     */
    fun logoutUser(context: Context) {
        Firebase.auth.signOut()

        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit { remove("user_email") }

        val intent = Intent(context, WelcomeActivity::class.java)
        context.startActivity(intent)
    }

    fun checkUserAuth(context: Context): Boolean {
        val user = Firebase.auth.currentUser
        var email = getSavedEmail(context)

        return user != null && email != null
    }

    /**
     * Chama as funções de validação dos campos do cadastro,
     * para descobrir se todos são validos
     */
     fun fieldValidation(name: String, email: String, password: String): Boolean {
        if (emptyRegistrationFields(name, email, password)
            && emailValidation(email)
            && passwordInvalid(password)) {
            return false
        }

        return true
    }
}
