package com.example.themeapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.themeapp.activities.MainActivity
import com.example.themeapp.models.Restaurant
import com.example.themeapp.navigation.Screen
import kotlinx.coroutines.NonDisposableHandle.parent

@Composable
fun RestaurantCard(restaurant: Restaurant,navController: NavController){
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .width(250.dp)
            .height(250.dp)
            .clickable {
                navController.navigate(Screen.DetailScreen.withArgs(restaurant.id))
            }
    ){
        Column {
            val image = loadPicture(context = LocalContext.current, url =restaurant.img , defaultImg =com.example.themeapp.R.drawable.ic_img ).value
            image?.let{img->

                Image(bitmap = img.asImageBitmap(), contentDescription = null,
                    contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.55f)
                )
            }
            Text(
                text = restaurant.name,
                fontSize = MaterialTheme.typography.h6.fontSize,
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = restaurant.location.display_address[0],
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                modifier = Modifier.padding(start = 5.dp),
                fontWeight = FontWeight.Normal
            )
            var categories = ""
            restaurant.categories.forEachIndexed { index, category ->
                if(index != restaurant.categories.lastIndex)
                    categories = category.title + ","
                else{
                    categories += category.title
                }
            }
            Text(
                text = categories,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                modifier = Modifier.padding(start = 5.dp),
                fontWeight = FontWeight.Normal
            )
        }
    }
}