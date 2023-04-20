package com.example.themeapp.view

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.themeapp.activities.MainActivity
import com.example.themeapp.activities.MainActivity.Companion.mainViewModel
import com.example.themeapp.activities.MainActivity.Companion.restaurantDetailsViewModel
import com.example.themeapp.models.Review
import com.example.themeapp.network.RestaurantApi
import com.example.themeapp.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddReview(tokenUser:String,id:String) {
    var rating by remember { mutableStateOf(0f) }
    var message by remember { mutableStateOf("") }
    // Valoarea de control pentru resetarea rating-ului
    val resetRating = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Add Review",
            fontSize = MaterialTheme.typography.h5.fontSize,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))

        RatingBar(
            maxRating = 5,
            rating = rating,
            onRatingChanged = { newRating ->
                rating = newRating
            },
            resetRating = resetRating.value
        )

        // Review Comment
        TextField(
            value = message,
            onValueChange = {
                            message = it
            },
            label = { Text(text = "Enter your review comment") },
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
        // Formatare cu o precizie de o zecimală
        val ratingValue = String.format("%.1f", rating.toDouble())
        val coroutineScope = rememberCoroutineScope()
        // Submit Button
        Button(
            onClick = {
                restaurantDetailsViewModel.addReview(
                    Review(
                        id = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        rating = ratingValue.toDouble(),
                        message = message,
                        idRestaurant = restaurantDetailsViewModel.restaurant.value.id
                    ), tokenUser = tokenUser
                )
                restaurantDetailsViewModel.reviews.value.add( Review(
                    id = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    rating = ratingValue.toDouble(),
                    message = message,
                    idRestaurant = restaurantDetailsViewModel.restaurant.value.id
                ))
                    //mainViewModel.getRestaurantDetail(id = id, tokenUser = tokenUser)
                    resetRating.value = true
                    rating = 0f
                    message = ""

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun RatingBar(
    maxRating: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    resetRating: Boolean
) {
    val selectedRating = remember { mutableStateOf(rating) }
    // Utilizăm LaunchedEffect pentru a observa valoarea rating-ului din exterior și pentru a o actualiza pe UI thread
    LaunchedEffect(rating, resetRating) {
        if (resetRating) {
            selectedRating.value = rating // Resetarea valorii rating-ului
        } else {
            selectedRating.value = rating // Actualizarea valorii rating-ului din exterior
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxRating) {
            // Determinăm imaginea de stea în funcție de rating și poziția curentă
            val imageVector = if (selectedRating.value >= i) {
                Icons.Filled.Star
            } else if (selectedRating.value >= i - 0.5f) {
                Icons.Filled.StarHalf
            } else {
                Icons.Outlined.Star
            }

            // Adăugăm imaginea de stea
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = if (selectedRating.value >= i) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            // Calculăm rating-ul selectat în funcție de coordonatele gestului de atingere
                            val ratingOffset = offset.x / (24.dp.toPx() * 1.5f)
                            val newRating = i - 0.5f + ratingOffset
                            selectedRating.value = newRating.coerceIn(0f, maxRating.toFloat())
                            onRatingChanged(selectedRating.value)
                        }
                    }
            )
        }
    }
}




















