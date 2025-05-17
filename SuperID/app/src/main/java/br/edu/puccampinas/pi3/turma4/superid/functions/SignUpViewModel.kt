package br.edu.puccampinas.pi3.turma4.superid.functions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignUpViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _agreeTerms = MutableStateFlow(false)
    val agreeTerms: StateFlow<Boolean> = _agreeTerms

    fun onNameChange(value: String) { _name.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onAgreeTermsChange(value: Boolean) {_agreeTerms.value = value}
}