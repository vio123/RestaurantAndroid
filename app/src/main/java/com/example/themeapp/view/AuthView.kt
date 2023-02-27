package com.example.themeapp.view

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.themeapp.activities.MainActivity
import com.example.themeapp.login.AuthResultContract
import com.example.themeapp.viewmodels.AuthViewModel
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun AuthView(errorText:String?,
             onClick:() -> Unit){
            GoogleSignInButtonUi(text = "Sign Up With Google",
                loadingText = "Signing In....",
                onClicked = {onClick()})
            errorText?.let {
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = it)
            }
}
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun AuthScreen(authViewModel: AuthViewModel){

    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf<String?>(null) }
    val user by remember(authViewModel){authViewModel.user}.collectAsState()
    val signInRequestCode = 1

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()){
                task ->
            try {
                val account = task?.getResult(ApiException::class.java)
                if (account==null){
                    text = "Google sign in failed"
                }else{
                    coroutineScope.launch {
                        authViewModel.signIn(email = account.email!!,displayName = account.displayName!!,profilePicture = account.photoUrl.toString())
                    }
                }
            }catch (e: ApiException){
                text="Google sign in failed"
            }
        }
    AuthView(errorText = text,onClick = {text=null
        authResultLauncher.launch(signInRequestCode)
    })
    user?.let{
        val intent = Intent(LocalContext.current,MainActivity::class.java).also {i ->
            i.putExtra("email",it.email)
            i.putExtra("displayName",it.displayName)
            i.putExtra("pictureUrl",it.pictureUrl)
            LocalContext.current.startActivity(i)
        }
    }
}