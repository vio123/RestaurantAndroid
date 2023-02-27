package com.example.themeapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.themeapp.models.BestDeals
import com.example.themeapp.models.Restaurant
import com.example.themeapp.navigation.Screen

@Composable
fun BestDealItem(bestDeals: BestDeals, navController: NavController){
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .width(250.dp)
            .height(250.dp)
            .clickable {
                //navController.navigate(Screen.DetailScreen.withArgs(restaurant.id))
            }
    ){
        ConstraintLayout() {
            val imgRef = createRef()
            val image = loadPicture(context = LocalContext.current, url =bestDeals.img , defaultImg =com.example.themeapp.R.drawable.ic_img ).value
            image?.let {img->

                Image(bitmap = img.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .constrainAs(imgRef){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                )
            }
            val columnRef = createRef()
            Column(modifier = Modifier.constrainAs(columnRef){
                start.linkTo(parent.start, margin = 10.dp)
                bottom.linkTo(parent.bottom)
            }) {
                Text(
                    text = bestDeals.category,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    modifier = Modifier
                        .padding(5.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text( text = "${bestDeals.nrPlaces} places",
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    modifier = Modifier
                        .padding(5.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            }
        }
    }
}