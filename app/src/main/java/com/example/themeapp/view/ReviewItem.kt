package com.example.themeapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.themeapp.activities.MainActivity.Companion.mainViewModel
import com.example.themeapp.models.Review
import com.example.themeapp.models.User

@Composable
fun ReviewItem(review: Review,user: User){
    Card(
        elevation = 40.dp,
        shape = RoundedCornerShape(15.dp),
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=10.dp, bottom = 10.dp)
    ){
        Column( modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp, start = 10.dp)) {
            Row{
                val image = loadPicture(context = LocalContext.current, url = "" , defaultImg =com.example.themeapp.R.drawable.ic_img ).value
                image?.let {img->

                    Image(bitmap = img.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop,            // crop the image if it's not a square
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)                       // clip to the circle shape
                            .border(2.dp, Color.Gray, CircleShape) // add a border (optional))
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    Text(text = user.name, fontSize = MaterialTheme.typography.h5.fontSize)
                    Row() {
                        Text(text = review.message, fontSize = MaterialTheme.typography.subtitle1.fontSize)
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "", fontSize = MaterialTheme.typography.subtitle1.fontSize, modifier = Modifier
                            .padding(end = 10.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = review.rating.toString(), fontSize = MaterialTheme.typography.h6.fontSize, modifier = Modifier
                            .padding(end = 10.dp))
                    }
                }
            }
        }
    }
}