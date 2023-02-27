package com.example.themeapp.models

import android.graphics.drawable.PaintDrawable
import androidx.compose.ui.graphics.painter.Painter

data class LoginPager(
    val logo: Painter,
    val title: String,
    val subTitle: String
)
