package com.example.themeapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.themeapp.R
import com.example.themeapp.ui.theme.ThemeAppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, LoginActivity::class.java)
        val intentMain = Intent(this,MainActivity::class.java)
        object : CountDownTimer(3000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                Log.e("test123","finished")
                val auth = Firebase.auth
                if(auth.currentUser != null){
                    startActivity(intentMain)
                    finish()
                }else {
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
        setContent {
            ThemeAppTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(){
                    ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val logoRef = createRef()
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logo),
                            contentDescription = "logo",
                            modifier = Modifier.fillMaxSize(0.2f).constrainAs(logoRef){
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Logo() {
   Icon(painter = painterResource(id = R.drawable.ic_logo), contentDescription = "logo")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    ThemeAppTheme {
       Logo()
    }
}