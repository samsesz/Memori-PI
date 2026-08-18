package com.memori.app.ui.screens.auth

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: android.app.Application) : AndroidViewModel(application) {

    private val _context = getApplication<android.app.Application>()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = withContext(Dispatchers.IO) {
                    com.memori.app.data.remote.RetrofitClient.api.login(
                        com.memori.app.data.remote.LoginRequest(email, pass)
                    )
                }

                if (response.isSuccessful) {
                    val token = response.body()!!.token

                    val payload = token.split(".")[1]
                    val decodedBytes = android.util.Base64.decode(payload, android.util.Base64.URL_SAFE)
                    val userId = org.json.JSONObject(String(decodedBytes)).getString("id")

                    com.memori.app.data.remote.SessionManager.salvarSessao(_context, userId, token)

                    _authState.value = AuthState.Success("Bem-vindo!")
                    onSuccess()

                } else {
                    val mensagem = when (response.code()) {
                        401 -> "Email ou senha inválidos"
                        404 -> "Usuário não encontrado"
                        else -> "Erro ao fazer login"
                    }
                    _authState.value = AuthState.Error(mensagem)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro de conexão com o servidor")
            }
        }
    }

    fun register(email: String, pass: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // TODO: implementar cadastro via API igual ao login
            _authState.value = AuthState.Success("Conta criada (Modo Teste)!")
            onSuccess()
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}