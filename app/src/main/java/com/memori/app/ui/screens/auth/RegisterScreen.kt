package com.memori.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memori.app.ui.theme.RedPrimary
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreeTerms by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        // Simulating Top Illustrations
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = (-20).dp, y = (-20).dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD54F).copy(alpha = 0.5f))
        )
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = (-30).dp)
                .clip(CircleShape)
                .background(Color(0xFF81C784).copy(alpha = 0.5f))
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = 100.dp, y = 200.dp)
                .clip(CircleShape)
                .background(Color(0xFF4DB6AC).copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Cadastro",
                color = RedPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Form Fields
            RegistrationField("Nome completo:", name, "Digite o seu nome completo", enabled = authState !is AuthState.Loading) { name = it }
            RegistrationField("E-mail:", email, "Digite o seu e-mail", enabled = authState !is AuthState.Loading) { email = it }
            RegistrationField("Usuário:", username, "Digite o seu usuário", enabled = authState !is AuthState.Loading) { username = it }
            RegistrationField("Senha:", password, "Digite a sua senha", isPassword = true, enabled = authState !is AuthState.Loading) { password = it }

            // Terms Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = agreeTerms,
                    onClick = { if (authState !is AuthState.Loading) agreeTerms = !agreeTerms },
                    colors = RadioButtonDefaults.colors(selectedColor = RedPrimary),
                    enabled = authState !is AuthState.Loading
                )
                Text(
                    "Concordo com os termos de uso",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                        if (agreeTerms) {
                            viewModel.register(email, password, name, onNavigateToHome)
                        } else {
                            Toast.makeText(context, "Aceite os termos de uso", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                shape = RoundedCornerShape(28.dp),
                enabled = authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Cadastrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    "Já tenho uma conta!",
                    color = RedPrimary,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom Logo Placeholder
            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                Box(Modifier.size(20.dp).background(Color(0xFFFF5252), RoundedCornerShape(2.dp)))
                Spacer(Modifier.width(4.dp))
                Box(Modifier.size(20.dp).background(Color(0xFFFFC107), RoundedCornerShape(2.dp)))
                Spacer(Modifier.width(4.dp))
                Box(Modifier.size(20.dp).background(Color(0xFF4CAF50), RoundedCornerShape(2.dp)))
            }
        }
    }
}

@Composable
fun RegistrationField(
    label: String,
    value: String,
    placeholder: String,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(
            text = label,
            color = RedPrimary,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            placeholder = { Text(placeholder, fontSize = 12.sp, color = Color.LightGray) },
            shape = RoundedCornerShape(26.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = RedPrimary.copy(alpha = 0.5f),
                focusedBorderColor = RedPrimary
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            singleLine = true,
            enabled = enabled
        )
    }
}
