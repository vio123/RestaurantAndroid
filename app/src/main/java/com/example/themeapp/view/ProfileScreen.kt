package com.example.themeapp.view

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.themeapp.activities.LoginActivity
import com.example.themeapp.activities.MainActivity
import com.example.themeapp.activities.dark
import com.example.themeapp.login.getGoogleSignInClient
import com.example.themeapp.models.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController){
       Surface() {
              ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                     val profilePictureRef = createRef()
                     val image = loadPicture(context = LocalContext.current, url =MainActivity.profilePictureUrl , defaultImg =com.example.themeapp.R.drawable.ic_img ).value
                     image?.let {img->

                            Image(bitmap = img.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop,            // crop the image if it's not a square
                                   modifier = Modifier
                                          .size(100.dp)
                                          .clip(CircleShape)
                                          .border(2.dp, Color.Gray, CircleShape)
                                          .constrainAs(profilePictureRef) {
                                                 start.linkTo(parent.start)
                                                 top.linkTo(parent.top, margin = 20.dp)
                                                 end.linkTo(parent.end)
                                          }
                            )
                     }
                     val emailRef = createRef()
                     Text(
                            text = MainActivity.email,
                            modifier = Modifier.constrainAs(emailRef){
                                   top.linkTo(profilePictureRef.bottom, margin = 20.dp)
                                   start.linkTo(parent.start, margin = 10.dp)
                                   end.linkTo(parent.end, margin = 10.dp)
                                   width = Dimension.fillToConstraints
                            }
                     )
                     val nameRef = createRef()
                     Text(
                            text = MainActivity.displayName,
                            modifier = Modifier.constrainAs(nameRef){
                                   top.linkTo(emailRef.bottom, margin = 20.dp)
                                   start.linkTo(parent.start, margin = 10.dp)
                                   end.linkTo(parent.end, margin = 10.dp)
                                   width = Dimension.fillToConstraints
                            }
                     )
                     val logOutRef = createRef()
                     val context = LocalContext.current
                     val activity = (LocalContext.current as? Activity)
                     Button(
                            onClick = {
                                   getGoogleSignInClient(context).signOut()
                                   context.startActivity(Intent(context,LoginActivity::class.java))
                                   activity?.finish()
                                      },
                            modifier = Modifier.constrainAs(logOutRef){
                                   top.linkTo(nameRef.bottom, margin = 20.dp)
                                   start.linkTo(parent.start, margin = 10.dp)
                                   end.linkTo(parent.end, margin = 10.dp)
                            }
                     ) {
                            Text(text = "Logout")
                     }
              }
       }
}