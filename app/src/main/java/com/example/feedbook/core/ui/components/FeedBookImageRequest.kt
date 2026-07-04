package com.example.feedbook.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest

@Composable
fun rememberFeedBookImageRequest(model: Any?): ImageRequest {
    val context = LocalContext.current
    val cacheKey = model?.toString()
    return remember(context, cacheKey) {
        ImageRequest.Builder(context)
            .data(model)
            .crossfade(180)
            .apply {
                if (cacheKey != null) {
                    memoryCacheKey(cacheKey)
                    diskCacheKey(cacheKey)
                }
            }
            .build()
    }
}
