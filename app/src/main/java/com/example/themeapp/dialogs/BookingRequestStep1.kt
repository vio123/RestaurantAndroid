package com.example.themeapp.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.themeapp.activities.MainActivity.Companion.context
import com.example.themeapp.activities.MainActivity.Companion.restaurantDetailsViewModel
import com.example.themeapp.components.ScrollableRowWithNavigationButtons
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingRequestStep1(
    onDismiss: () -> Unit
) {
    val year:Int
    val month:Int
    val day:Int

    val hour:Int
    val minute:Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    hour = calendar.get(Calendar.HOUR_OF_DAY)
    minute = calendar.get(Calendar.MINUTE)
    calendar.time = Date()
    val datePickerDialog = DatePickerDialog(
        context,
        {
                _: DatePicker,year:Int,month:Int,dayOfMonth:Int ->
            restaurantDetailsViewModel.date.value = "$dayOfMonth/$month/$year"

        },year,month,day
    )
    val timePickerDialog = TimePickerDialog(
        context,
        {
            _,hour:Int,minute:Int ->
            restaurantDetailsViewModel.time.value = "$hour:$minute"
        },hour,minute,false
    )
    // Lista de elemente pentru slider
    val items = remember { listOf(1, 2, 3, 4, 5,6,7,8,9) }

    // Handler pentru evenimentul de selectare a unui element
    val onItemSelected: (Int) -> Unit = { newIndex ->
        restaurantDetailsViewModel.selectedIndex.value = newIndex
    }
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(Color.White)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                ) {
                    Text(text = "THE NUMBER OF PEOPLE", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    ScrollableRowWithNavigationButtons(
                        items = items,
                        selectedIndex = restaurantDetailsViewModel.selectedIndex.value,
                        onItemSelected = onItemSelected,
                    )
                    Text(text = "RESERVATION DATE", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(Color.Transparent)
                            .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium),
                        elevation = 0.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { datePickerDialog.show() }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = restaurantDetailsViewModel.date.value,
                                style = MaterialTheme.typography.button,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Add",
                                tint = Color.Black
                            )
                        }
                    }
                    Text(text = "RESERVATION TIME", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(Color.Transparent)
                            .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium),
                        elevation = 0.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { timePickerDialog.show() }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = restaurantDetailsViewModel.time.value,
                                style = MaterialTheme.typography.button,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Add",
                                tint = Color.Black
                            )
                        }
                    }
                    Button(onClick = {

                                     onDismiss()

                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Continue")
                    }
                }
            }
        }
    }
}


