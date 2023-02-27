package com.example.themeapp.view

import android.os.Build
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.Space
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.text.util.LinkifyCompat
import androidx.navigation.NavController
import com.example.themeapp.activities.MainActivity.Companion.mainViewModel
import com.example.themeapp.viewmodels.RestaurantApiStatus
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import java.time.DayOfWeek
import java.time.LocalDateTime
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreenConstraint(id:String,navController: NavController){
    mainViewModel.getRestaurantDetail(id)
    mainViewModel.getReviews(id)
    var address= ""
    var isOpen = false
    try{
        address = mainViewModel.restaurant.value.location?.display_address?.get(0) ?: ""
        isOpen = mainViewModel.restaurant.value.hours[0].isOpenNow
    }catch (e:Exception){
    }
    Surface() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ){
            val image = loadPicture(
                context = LocalContext.current,
                url = mainViewModel.restaurant.value.img,
                defaultImg = com.example.themeapp.R.drawable.ic_img
            ).value
            image?.let { img ->
                val imgRestaurant = createRef()
                Image(
                    bitmap = img.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .constrainAs(imgRestaurant) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                )
                val backBtn = createRef()
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back",
                    tint = Color.White,
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .constrainAs(backBtn) {
                            top.linkTo(parent.top, margin = 10.dp)
                            start.linkTo(parent.start, margin = 10.dp)
                        }
                )
                val color:Color = if(mainViewModel.restaurant.value.img.isEmpty()){
                    MaterialTheme.colors.primary
                }else{
                    Color.White
                }
                val restaurantName = createRef()
                val rating = createRef()
                Text(
                    text = mainViewModel.restaurant.value.name,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    color = color,
                    modifier = Modifier
                        .constrainAs(restaurantName){
                            start.linkTo(parent.start, margin = 10.dp)
                            bottom.linkTo(rating.top)
                        }
                )
                val claimRef = createRef()
                RatingBar(
                    value = mainViewModel.restaurant.value.rating.toFloat(),
                    config = RatingBarConfig()
                        .style(RatingBarStyle.HighLighted),
                    onValueChange = {
                    },
                    onRatingChanged = {

                    },
                    modifier = Modifier.constrainAs(rating){
                        start.linkTo(parent.start,margin = 10.dp)
                        bottom.linkTo(claimRef.top)
                    }
                )
                val reviewNumber = createRef()
                Text(
                    text = mainViewModel.reviews.value.reviews.size.toString()+" reviews",
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    color = color,
                    modifier = Modifier.constrainAs(reviewNumber){
                        bottom.linkTo(claimRef.top)
                        start.linkTo(rating.end, margin = 10.dp)
                        top.linkTo(restaurantName.bottom)
                    }
                )
                var categories =""
                mainViewModel.restaurant.value.categories.forEachIndexed { index, category ->
                    if(index != mainViewModel.restaurant.value.categories.lastIndex)
                        categories = category.title + ","
                    else{
                        categories += category.title
                    }
                }
                val claimed:String = if(mainViewModel.restaurant.value.isClaimed){
                    "Claimed, ${mainViewModel.restaurant.value.price}, $categories"
                }else{
                    "Not Claimed, ${mainViewModel.restaurant.value.price}, $categories"
                }
                val statusRef = createRef()
                Text(
                    text =claimed,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.constrainAs(claimRef){
                        start.linkTo(parent.start,margin = 10.dp)
                        bottom.linkTo(statusRef.top)
                    }
                )
                val status = if(isOpen){
                    "Open"
                }else{
                    "Closed"
                }
                val statusColor:Color = if(isOpen){
                    Color.Green
                }else{
                   Color.Red
                }
                Text(
                    text =status,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = statusColor,
                    modifier = Modifier.constrainAs(statusRef){
                        start.linkTo(parent.start,margin = 10.dp)
                        bottom.linkTo(imgRestaurant.bottom)
                    }
                )
                val current = LocalDateTime.now()
                val dayOfWeek: DayOfWeek = DayOfWeek.from(current)
                val listItem = createRef()
                if(mainViewModel.statusMap.value == RestaurantApiStatus.DONE) {
                    LazyColumn(modifier = Modifier.constrainAs(listItem) {
                        top.linkTo(imgRestaurant.bottom, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                        end.linkTo(parent.end, margin = 10.dp)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }) {
                        try {
                            item {
                                Text(
                                    text = "Location & Hours",
                                    fontSize = MaterialTheme.typography.h5.fontSize,
                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp))
                            }
                            item{
                                val maps = createRef()
                                val lat:Double = mainViewModel.restaurant.value.coordinates.latitude
                                val long:Double = mainViewModel.restaurant.value.coordinates.longitude
                                if(mainViewModel.statusMap.value == RestaurantApiStatus.DONE) {
                                    val restaurantLocation = LatLng(lat, long)
                                    val cameraPositionState = rememberCameraPositionState {
                                        position = CameraPosition.fromLatLngZoom(restaurantLocation, 18f)
                                    }
                                    GoogleMap(
                                        modifier = Modifier
                                            .fillMaxHeight(0.3f)
                                            .constrainAs(maps) {
                                                start.linkTo(parent.start, margin = 10.dp)
                                                end.linkTo(parent.end, margin = 10.dp)
                                                width = Dimension.fillToConstraints
                                            },
                                        cameraPositionState = cameraPositionState
                                    ) {

                                        Marker(
                                            position = restaurantLocation,
                                            title = mainViewModel.restaurant.value.name,
                                            snippet = address
                                        )
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp))
                                Row(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .fillMaxWidth(),
                                    Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Call", fontSize = MaterialTheme.typography.h5.fontSize)
                                    MyContent(text = mainViewModel.restaurant.value.phone)
                                }
                            }
                            item {
                                Row(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .fillMaxWidth(),
                                    Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Cuisines", fontSize = MaterialTheme.typography.h5.fontSize)
                                    var text =""
                                    mainViewModel.restaurant.value.categories.forEachIndexed { index, category ->
                                        if(index!= mainViewModel.restaurant.value.categories.lastIndex) {
                                            text += category.title
                                            text+=","
                                        }else
                                            text+=category.title
                                    }
                                    MyContent(text = text)
                                }
                            }
                            items(mainViewModel.restaurant.value.hours[0].open) {item ->
                                HourComponent(hour = item, day = item.day, currentDay = dayOfWeek)
                            }
                            item{
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp))
                                Text(
                                    text = "Photos",
                                    fontSize = MaterialTheme.typography.h5.fontSize,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            item {
                                LazyRow(modifier = Modifier.padding(top = 10.dp)) {
                                    items(items = mainViewModel.restaurant.value.photos){photo->
                                        val image = loadPicture(
                                            context = LocalContext.current,
                                            url = photo,
                                            defaultImg = com.example.themeapp.R.drawable.ic_img
                                        ).value
                                        image?.let { img ->
                                            Image(
                                                bitmap = img.asImageBitmap(),
                                                contentDescription = null,
                                                contentScale= ContentScale.FillHeight,
                                                modifier = Modifier
                                                    .fillMaxWidth(0.5f)
                                                    .fillMaxHeight(0.30f)
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp))
                                Text(
                                    text = "Reviews",
                                    fontSize = MaterialTheme.typography.h5.fontSize,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            items(items = mainViewModel.reviews.value.reviews){review->
                                ReviewItem(review = review)
                            }
                        } catch (e: Exception) {

                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MyContent(text:String){

    val mContext = LocalContext.current
    val mCustomLinkifyText = remember { TextView(mContext) }


    Column() {
        AndroidView(factory = { mCustomLinkifyText }) { textView ->
            textView.text = text
            textView.textSize = 20F
            LinkifyCompat.addLinks(textView, Linkify.ALL)
            textView.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}