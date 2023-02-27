package com.example.themeapp.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.themeapp.models.Hour
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourComponent(hour: Hour, day:Int, currentDay: DayOfWeek){
    Surface(
        modifier =Modifier
            .fillMaxWidth()
    ){
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val dayName:String = when(day){
                0 -> {
                    "Mon"
                }
                1 -> {
                    "Tue"
                }
                2 -> {
                    "Wed"
                }
                3 -> {
                    "Thu"
                }
                4 -> {
                    "Fri"
                }
                5 -> {
                    "Sat"
                }
                6 -> {
                    "Sun"
                }
                else -> {
                    ""
                }
            }
            val asterisc = if(day == currentDay.value-1){
                "*"
            }else{
                ""
            }
            val dayRef = createRef()
            val hoursRef = createRef()
            Text(
                text = asterisc+dayName,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                modifier = Modifier.constrainAs(dayRef){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = 10.dp)
                    end.linkTo(hoursRef.start)
                    width = Dimension.fillToConstraints
                }
            )
            val startHour = "${hour.start[0]}${hour.start[1]}:${hour.start[2]}${hour.start[3]}"
            val endHour = "${hour.end[0]}${hour.end[1]}:${hour.end[2]}${hour.end[3]}"
            Text(
                text = "$startHour - $endHour",
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                modifier = Modifier.constrainAs(hoursRef){
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(dayRef.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}