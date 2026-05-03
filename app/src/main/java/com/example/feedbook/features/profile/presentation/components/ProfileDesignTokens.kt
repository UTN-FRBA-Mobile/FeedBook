package com.example.feedbook.features.profile.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal object ProfileColors {
    val Background = Color(0xFFFBF9F8)
    val Surface = Color(0xFFFBF9F8)
    val SurfaceStrong = Color(0xFF03192E)
    val Border = Color(0x4DC4C6CD)
    val BorderDashed = Color(0x80C4C6CD)
    val Divider = Color(0xFFE3E2E2)
    val PrimaryText = Color(0xFF1B1C1C)
    val SecondaryText = Color(0xFF43474D)
    val Accent = Color(0xFF7C5730)
    val AccentSoft = Color(0xFFE6E2DB)
    val ArchiveText = Color(0xFFB4C8E4)
    val TodayOutline = Color(0x80C4C6CD)
}

internal object ProfileTypography {
    val AppTitle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-1.2).sp
    )
    val HeroName = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 38.4.sp
    )
    val SectionTitle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 30.sp
    )
    val LargeBookTitle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 40.sp
    )
    val StatNumber = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        lineHeight = 52.8.sp
    )
    val Body = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    val Label = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 14.4.sp,
        letterSpacing = 0.24.sp
    )
    val LabelUppercase = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 14.4.sp,
        letterSpacing = 1.2.sp
    )
    val SmallCaps = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 1.4.sp
    )
    val Button = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.7.sp
    )
}
