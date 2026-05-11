package com.example.feedbook.features.profile.presentation.components

import com.example.feedbook.core.ui.theme.FeedBookColors
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal object ProfileColors {
    val Background = FeedBookColors.Background
    val Surface = FeedBookColors.Surface
    val SurfaceStrong = FeedBookColors.SurfaceStrong
    val Border = FeedBookColors.Border
    val BorderDashed = FeedBookColors.BorderDashed
    val Divider = FeedBookColors.Divider
    val PrimaryText = FeedBookColors.PrimaryText
    val SecondaryText = FeedBookColors.SecondaryText
    val Accent = FeedBookColors.Accent
    val AccentSoft = FeedBookColors.AccentSoft
    val ArchiveText = FeedBookColors.ArchiveText
    val TodayOutline = FeedBookColors.TodayOutline
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
