package com.example.themeapp.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ScrollableRowWithNavigationButtons(
    items: List<Int>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(maxOf(scrollState.value - 100, 0))
                    }
                },
                modifier = Modifier
                    .clip(CircleShape), // Use CircleShape for clipping
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Previous",
                    modifier = Modifier.size(16.dp)
                )
            }


            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .horizontalScroll(scrollState)
            ) {
                Row {
                    repeat(items.size) { index ->
                        val isSelected = selectedIndex == index
                        ButtonSliderItem(
                            item = items[index],
                            isSelected = isSelected,
                            onItemClick = { onItemSelected(index) }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(minOf(scrollState.value + 100, scrollState.maxValue))
                    }
                },
                modifier = Modifier
                    .clip(CircleShape), // Use CircleShape for clipping
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
}
@Composable
fun ButtonSliderItem(
    item: Int,
    isSelected: Boolean,
    onItemClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colors.primaryVariant else Color.Transparent
    val contentColor = if (isSelected) Color.White else MaterialTheme.colors.onSecondary

    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .padding(8.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onItemClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item.toString(),
            color = contentColor,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

