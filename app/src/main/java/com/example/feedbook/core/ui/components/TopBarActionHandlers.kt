package com.example.feedbook.core.ui.components

import androidx.compose.runtime.compositionLocalOf

internal val LocalScannerClickHandler = compositionLocalOf<() -> Unit> { {} }
