package com.example.themeapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.themeapp.ui.theme.ThemeAppTheme

@Composable
fun IconText(
    icon:ImageVector,
    text:String
){
    Column(modifier = Modifier.padding(20.dp)) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(36.dp))
        }
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview1() {
    ThemeAppTheme {
        IconText(
            icon = Icons.Default.Person,
            text = "3 pers."
        )
    }
}