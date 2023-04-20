package com.example.themeapp.view

import android.os.Build
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.text.util.LinkifyCompat
import androidx.navigation.NavController
import com.example.themeapp.R
import com.example.themeapp.activities.MainActivity.Companion.mainViewModel
import com.example.themeapp.activities.MainActivity.Companion.restaurantDetailsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
fun DetailScreen(id: String, navController: NavController) {
    val alreadyLoaded = remember {
        mutableStateOf(false)
    }
    val token = remember {
        mutableStateOf("")
    }
    val startPage = remember {
        mutableStateOf(0)
    }
    // Starea care controlează afișarea sau ascunderea dialogului
    val showDialog = remember { mutableStateOf(false) }
    val auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser
    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
        if (tokenTask.isSuccessful && !alreadyLoaded.value) {
            token.value = tokenTask.result?.token.toString()
            restaurantDetailsViewModel.getRestaurantDetail(id, tokenUser = token.value)
            restaurantDetailsViewModel.getReviews(id, tokenUser = token.value)
            alreadyLoaded.value = true
        }
    }
    if (restaurantDetailsViewModel.showDialog.value) {
        ShowImagePopup(images = restaurantDetailsViewModel.restaurant.value.photos, startPage.value)
    }
    Surface {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (showDialog.value) {
                // Afișează dialogul personalizat
                CustomDialog(
                    title = "Titlu dialog",
                    content = "Conținut dialog",
                    onDismiss = { showDialog.value = false }
                )
            }
            val image = loadPicture(
                context = LocalContext.current,
                url = restaurantDetailsViewModel.restaurant.value.img,
                defaultImg = R.drawable.ic_img
            ).value
            val imgRestaurant = createRef()
            image?.asImageBitmap()?.let { img ->
                Image(
                    bitmap = img,
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
            }
            val backBtn = createRef()
            val color: Color = if (restaurantDetailsViewModel.restaurant.value.img.isEmpty()) {
                MaterialTheme.colors.primary
            } else {
                Color.White
            }
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back",
                tint = color,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    }
            )
            val restaurantName = createRef()
            val rating = createRef()
            Text(
                text = restaurantDetailsViewModel.restaurant.value.name,
                fontSize = MaterialTheme.typography.h5.fontSize,
                color = color,
                modifier = Modifier
                    .constrainAs(restaurantName) {
                        start.linkTo(parent.start, margin = 10.dp)
                        bottom.linkTo(rating.top)
                    }
            )
            val claimRef = createRef()
            RatingBar(
                value = restaurantDetailsViewModel.restaurant.value.rating.toFloat(),
                config = RatingBarConfig()
                    .style(RatingBarStyle.HighLighted),
                onValueChange = {

                },
                onRatingChanged = {

                },
                modifier = Modifier.constrainAs(rating) {
                    start.linkTo(parent.start, margin = 10.dp)
                    bottom.linkTo(claimRef.top)
                }
            )
            val reviewNumber = createRef()
            Text(
                text = restaurantDetailsViewModel.reviews.value.size.toString() + " reviews",
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                color = color,
                modifier = Modifier.constrainAs(reviewNumber) {
                    bottom.linkTo(claimRef.top)
                    start.linkTo(rating.end, margin = 10.dp)
                    top.linkTo(restaurantName.bottom)
                }
            )
            var categories = ""
            restaurantDetailsViewModel.restaurant.value.categories.forEachIndexed { index, category ->
                if (index != restaurantDetailsViewModel.restaurant.value.categories.lastIndex)
                    categories = category.title + ","
                else {
                    categories += category.title
                }
            }
            val claimed: String = if (restaurantDetailsViewModel.restaurant.value.isClaimed) {
                "Claimed, ${restaurantDetailsViewModel.restaurant.value.price}, $categories"
            } else {
                "Not Claimed, ${restaurantDetailsViewModel.restaurant.value.price}, $categories"
            }
            val statusRef = createRef()
            Text(
                text = claimed,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = Modifier.constrainAs(claimRef) {
                    start.linkTo(parent.start, margin = 10.dp)
                    bottom.linkTo(statusRef.top)
                }
            )
            val status = if (restaurantDetailsViewModel.restaurant.value.isOpen) {
                "Open"
            } else {
                "Closed"
            }
            val statusColor: Color = if (restaurantDetailsViewModel.restaurant.value.isOpen) {
                Color.Green
            } else {
                Color.Red
            }
            Text(
                text = status,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontWeight = FontWeight.Bold,
                color = statusColor,
                modifier = Modifier.constrainAs(statusRef) {
                    start.linkTo(parent.start, margin = 10.dp)
                    bottom.linkTo(imgRestaurant.bottom)
                }
            )
            var address = ""
            try {
                address =
                    restaurantDetailsViewModel.restaurant.value.location?.display_address?.get(0)
                        ?: ""
            } catch (e: java.lang.Exception) {

            }
            val current = LocalDateTime.now()
            val dayOfWeek: DayOfWeek = DayOfWeek.from(current)
            val listItem = createRef()
            LazyColumn(modifier = Modifier.constrainAs(listItem) {
                top.linkTo(imgRestaurant.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }) {
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showDialog.value = true
                        }
                    ) {
                        Text(text = "Request table")
                    }
                }
                item {
                    Text(
                        text = "Location & Hours",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                }
                item {
                    val maps = createRef()
                    val lat: Double =
                        restaurantDetailsViewModel.restaurant.value.coordinates.latitude
                    val long: Double =
                        restaurantDetailsViewModel.restaurant.value.coordinates.longitude
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
                            title = restaurantDetailsViewModel.restaurant.value.name,
                            snippet = address
                        )
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                    Row(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(),
                        Arrangement.SpaceBetween
                    ) {
                        Text(text = "Call", fontSize = MaterialTheme.typography.h5.fontSize)
                        MyContent(text = restaurantDetailsViewModel.restaurant.value.phone)
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
                        var text = ""
                        restaurantDetailsViewModel.restaurant.value.categories.forEachIndexed { index, category ->
                            if (index != restaurantDetailsViewModel.restaurant.value.categories.lastIndex) {
                                text += category.title
                                text += ","
                            } else
                                text += category.title
                        }
                        MyContent(text = text)
                    }
                }
                items(restaurantDetailsViewModel.restaurant.value.open) { item ->
                    HourComponent(hour = item, day = item.day, currentDay = dayOfWeek)
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    Text(
                        text = "Photos",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Bold,
                    )
                }
                item {
                    LazyRow(modifier = Modifier.padding(top = 10.dp)) {
                        itemsIndexed(items = restaurantDetailsViewModel.restaurant.value.photos) { index, photo ->
                            val image = loadPicture(
                                context = LocalContext.current,
                                url = photo,
                                defaultImg = R.drawable.ic_img
                            ).value
                            image?.let { img ->
                                Image(
                                    bitmap = img.asImageBitmap(),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillHeight,
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .fillMaxHeight(0.30f)
                                        .clickable {
                                            restaurantDetailsViewModel.showDialog.value = true
                                            startPage.value = index
                                        }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    AddReview(token.value, id)
                    Text(
                        text = "Reviews",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Bold,
                    )
                }
                items(items = restaurantDetailsViewModel.reviews.value.reversed()) { review ->
                    restaurantDetailsViewModel.getUser(id = review.id, tokenUser = token.value)
                    ReviewItem(review = review, user = restaurantDetailsViewModel.user.value)
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowImagePopup(images: List<String>, startPage: Int) {
    val state = rememberPagerState(initialPage = startPage)
    Dialog(onDismissRequest = { mainViewModel.showDialog.value = false }) {
        HorizontalPager(
            count = images.size,
            state = state
        ) { page ->
            val image = loadPicture(
                context = LocalContext.current,
                url = images[page],
                defaultImg = R.drawable.ic_img
            ).value
            image?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxSize(0.5f)
                )
            }
        }
        /*
        Row {
            Button(
                enabled = selectedImageIndex != 0,
                onClick = {
                // Show the previous image
                selectedImageIndex = (selectedImageIndex - 1).coerceAtLeast(0)
            }) {
                Text("Previous")
            }
            Button(
                enabled = selectedImageIndex != images.size - 1,
                onClick = {
                // Show the next image
                selectedImageIndex = (selectedImageIndex + 1).coerceAtMost(images.size - 1)
            }) {
                Text("Next")
            }
        }

         */
    }
}

@Composable
fun CustomDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.padding(10.dp).fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = content, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text(text = "OK")
                }
            }
        }
    }
}



@Composable
fun MyContent(text: String) {

    val mContext = LocalContext.current
    val mCustomLinkifyText = remember { TextView(mContext) }


    Column {
        AndroidView(factory = { mCustomLinkifyText }) { textView ->
            textView.text = text
            textView.textSize = 20F
            LinkifyCompat.addLinks(textView, Linkify.ALL)
            textView.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}