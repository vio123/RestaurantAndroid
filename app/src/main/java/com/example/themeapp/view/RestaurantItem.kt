package com.example.themeapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.themeapp.R
import com.example.themeapp.activities.Greeting
import com.example.themeapp.activities.MainActivity
import com.example.themeapp.models.Restaurant
import com.example.themeapp.navigation.Screen
import com.example.themeapp.ui.theme.ThemeAppTheme

@SuppressLint("UnrememberedMutableState")
@Composable
fun loadPicture(
    context: Context,
    url:String,
    @DrawableRes defaultImg:Int
):MutableState<Bitmap?>{
    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)
    Glide.with(context)
        .asBitmap()
        .load(defaultImg)
        .apply(RequestOptions.overrideOf(200,200))
        .into(object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

        })
    Glide.with(context)
        .asBitmap()
        .load(url)
        .apply(RequestOptions.overrideOf(200,200))
        .into(object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

        })
    return bitmapState
}
@Composable
fun RestautantItem(restaurant: Restaurant,navController: NavController){
    Card(
        elevation = 40.dp,
        shape = RoundedCornerShape(15.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                navController.navigate(Screen.DetailScreen.withArgs(restaurant.id))
            }
    ){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (imgRestaurant,restaurantName,address1,address2,rating) = createRefs()
            val image = loadPicture(context = LocalContext.current, url =restaurant.img , defaultImg =com.example.themeapp.R.drawable.ic_img ).value
            image?.let {img->

                Image(bitmap = img.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .constrainAs(imgRestaurant) {
                            start.linkTo(parent.start, margin = 10.dp)
                            top.linkTo(parent.top, margin = 10.dp)
                            bottom.linkTo(parent.bottom, margin = 10.dp)
                        }
                )
            }
            Text(
                text = restaurant.name,
                fontSize = MaterialTheme.typography.h5.fontSize,
                modifier = Modifier.constrainAs(restaurantName){
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(imgRestaurant.end, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                    bottom.linkTo(address1.top, margin = 1.dp)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                text = restaurant.location.display_address[0],
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                modifier = Modifier.constrainAs(address1){
                    top.linkTo(restaurantName.bottom, margin = 1.dp)
                    start.linkTo(imgRestaurant.end, margin = 10.dp)
                    end.linkTo(rating.start, margin = 2.dp)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                text = restaurant.rating.toString(),
                fontSize = MaterialTheme.typography.h6.fontSize,
                modifier = Modifier.constrainAs(rating){
                    top.linkTo(restaurantName.bottom, margin = 1.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                }
            )
            val favRef = createRef()
            Icon(
                painter = painterResource(id = R.drawable.ic_bookmark),
                contentDescription ="save",
                modifier = Modifier.constrainAs(favRef){
                    bottom.linkTo(parent.bottom, margin = 5.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                }
                    .clickable {
                       // MainActivity.roomViewModel.addRestaurant(restaurant)
                    }
            )
            Text(
                text = restaurant.location.display_address[1],
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                modifier = Modifier.constrainAs(address2){
                    top.linkTo(address1.bottom, margin = 1.dp)
                    start.linkTo(imgRestaurant.end, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}