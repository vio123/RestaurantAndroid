package com.example.themeapp.view

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.themeapp.activities.LoginActivity
import com.example.themeapp.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(viewModel: LoginViewModel,auth:FirebaseAuth){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        val (emailTextField, passwordTextField, loginButton) = createRefs()
        // Validate email address
        val isEmailValid = remember(viewModel.email.value) {
            Patterns.EMAIL_ADDRESS.matcher(viewModel.email.value).matches()
        }
        // Validate password
        val isPasswordValid = remember(viewModel.pass.value) {
            viewModel.pass.value.length >= 6
        }
        val focusManager = LocalFocusManager.current
        val titleRef = createRef()
        Text(
            text = "Login",
            fontSize = (screenHeight*0.05).sp,
            modifier = Modifier.constrainAs(titleRef){
                top.linkTo(parent.top, margin = (screenHeight*0.1).dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        TextField(
            modifier = Modifier.constrainAs(emailTextField) {
                top.linkTo(titleRef.bottom, margin = 30.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                width = Dimension.fillToConstraints
            },
            value = viewModel.email.value,
            onValueChange = { viewModel.email.value = it },
            label = { Text(text = "Email") },
            isError = viewModel.email.value.isNotEmpty() && !isEmailValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        TextField(
            modifier = Modifier.constrainAs(passwordTextField) {
                top.linkTo(emailTextField.bottom, 16.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                width = Dimension.fillToConstraints
            },
            value = viewModel.pass.value,
            onValueChange = { viewModel.pass.value = it },
            label = { Text(text = "Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = viewModel.pass.value.isNotEmpty() && !isPasswordValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { /* Perform login */ }
            ),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            },
        )
        Button(
            modifier = Modifier.constrainAs(loginButton) {
                top.linkTo(passwordTextField.bottom, 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onClick = {
                      auth.signInWithEmailAndPassword(viewModel.email.value,viewModel.pass.value)
                          .addOnCompleteListener {
                              if(it.isSuccessful){
                                  val user = auth.currentUser
                                  user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                                      if (tokenTask.isSuccessful) {
                                          val token = tokenTask.result?.token
                                          // Print the token to the console
                                          Log.d("test123", "Token: $token")
                                      }
                                  }
                              }
                          }
            },
            enabled = isEmailValid && isPasswordValid,
        ) {
            Text(text = "Log in")
        }
    }
}