package com.memori.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// import com.google.firebase.auth.FirebaseAuth // Comentado para teste sem Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    // private val auth: FirebaseAuth = FirebaseAuth.getInstance() // Comentado
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1000) // Simula tempo de rede
            
            // Simulação de login (Qualquer senha com mais de 3 dígitos passa)
            if (pass.length >= 3) {
                _authState.value = AuthState.Success("Bem-vindo (Modo Teste)!")
                onSuccess()
            } else {
                _authState.value = AuthState.Error("Senha muito curta para o teste!")
            }
            
            /* CÓDIGO FIREBASE COMENTADO:
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Success("Bem-vindo!")
                        onSuccess()
                    } else {
                        _authState.value = AuthState.Error(task.exception?.localizedMessage ?: "Erro ao entrar")
                    }
                }
            */
        }
    }

    fun register(email: String, pass: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1000) // Simula tempo de rede
            
            _authState.value = AuthState.Success("Conta criada (Modo Teste)!")
            onSuccess()

            /* CÓDIGO FIREBASE COMENTADO:
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Success("Conta criada!")
                        onSuccess()
                    } else {
                        _authState.value = AuthState.Error(task.exception?.localizedMessage ?: "Erro ao cadastrar")
                    }
                }
            */
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
    
    // fun getCurrentUser() = auth.currentUser // Comentado
}
