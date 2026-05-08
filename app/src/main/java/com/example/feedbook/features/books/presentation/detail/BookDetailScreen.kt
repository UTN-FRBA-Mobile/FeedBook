package com.example.feedbook.features.books.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.books.domain.model.Book

// ─── Paleta propia del diseño (independiente del theme) ───────────────────────
private val DesignNavy       = Color(0xFF1A2235)
private val DesignNavyMid    = Color(0xFF243047)
private val DesignGold       = Color(0xFFC9A84C)
private val DesignGoldLight  = Color(0xFFE8C76A)
private val DesignBackground = Color(0xFFF4F1EC)
private val DesignCard       = Color(0xFFFFFFFF)
private val DesignTextPrim   = Color(0xFF1A1A1A)
private val DesignTextSec    = Color(0xFF666666)
private val DesignTextMuted  = Color(0xFF999999)
private val DesignOrange     = Color(0xFFE07B39)
private val DesignDivider    = Color(0xFFE5E0D8)

// ─── Datos hardcodeados de ejemplo ───────────────────────────────────────────
private data class ReviewUi(
    val name: String,
    val initials: String,
    val avatarColor: Color,
    val rating: Float,
    val timeAgo: String,
    val text: String,
    val likes: Int,
    val comments: Int
)

private val sampleReviews = listOf(
    ReviewUi(
        name = "Julian Thorne",
        initials = "JT",
        avatarColor = Color(0xFF7B9EA6),
        rating = 5f,
        timeAgo = "2 days ago",
        text = "A hauntingly beautiful examination of the American Dream. The prose is like a finely woven tapestry. This remains one of my favorite re-reads for the sheer atmosphere Fitzgerald creates.",
        likes = 243,
        comments = 18
    ),
    ReviewUi(
        name = "Isabella V.",
        initials = "IV",
        avatarColor = Color(0xFFB87C6A),
        rating = 4f,
        timeAgo = "1 week ago",
        text = "The tragedy of Gatsby never fails to hit hard. The ending scene by the pool is still one of the most powerful moments in literature. Highly recommend this edition for the supplementary notes.",
        likes = 89,
        comments = 4
    )
)

// ─── Entry point ──────────────────────────────────────────────────────────────
@Composable
fun BookDetailScreen(
    viewModelFactory: androidx.lifecycle.ViewModelProvider.Factory,
    onBackClick: () -> Unit
) {
    val viewModel: BookDetailViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookDetailContent(
        state = state,
        onBackClick = onBackClick,
        onRetry = viewModel::loadBook
    )
}

// ─── Contenido principal ──────────────────────────────────────────────────────
@Composable
internal fun BookDetailContent(
    state: BookDetailState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignBackground)
    ) {
        when {
            state.isLoading -> LoadingContent()
            state.error != null -> ErrorContent(message = state.error, onRetry = onRetry)
            state.book != null -> BookDetailBody(
                book = state.book,
                onBackClick = onBackClick
            )
        }
    }
}

// ─── Loading ──────────────────────────────────────────────────────────────────
@Composable
private fun LoadingContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = DesignGold)
    }
}

// ─── Error ────────────────────────────────────────────────────────────────────
@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Algo salió mal", fontWeight = FontWeight.Bold, color = DesignTextPrim)
        Spacer(Modifier.height(8.dp))
        Text(message, color = DesignTextSec, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = DesignNavy)
        ) { Text("Reintentar") }
    }
}

// ─── Body principal ───────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookDetailBody(book: Book, onBackClick: () -> Unit) {
    var showProgressEditor by remember { mutableStateOf(false) }
    var currentPage by remember { mutableIntStateOf(142) }
    val totalPages = 218

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top Bar ──
        TopAppBar(
            title = { Text("FeedBook", fontWeight = FontWeight.Bold, color = DesignTextPrim) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = DesignTextPrim)
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.QrCode2, contentDescription = null, tint = DesignTextPrim)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = DesignTextPrim)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = DesignBackground)
        )

        // ── Portada ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().height(260.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(listOf(DesignNavy, DesignNavyMid))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("THE", color = DesignGold, fontSize = 12.sp, letterSpacing = 4.sp)
                        Text(
                            "GREAT\nGATSBY",
                            color = DesignGoldLight,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 38.sp,
                            letterSpacing = 2.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        HorizontalDivider(modifier = Modifier.width(80.dp), color = DesignGold, thickness = 1.dp)
                    }
                }
            }
        }

        // ── Info del libro ──
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            Text(
                "CLASSIC FICTION",
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = DesignTextMuted,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                book.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = DesignTextPrim,
                lineHeight = 30.sp
            )
            Text(
                "by ${book.author}",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = DesignTextPrim,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            StarRow(rating = 4.8f)
        }

        Spacer(Modifier.height(12.dp))

        // ── Botones ──
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Button(
                onClick = { showProgressEditor = !showProgressEditor },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DesignNavy)
            ) {
                Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Update Progress", fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DesignTextPrim)
            ) {
                Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Add to List", fontWeight = FontWeight.Medium)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Progress card ──
        if (showProgressEditor) {
            ProgressCard(
                currentPage = currentPage,
                totalPages = totalPages,
                onPageChange = { currentPage = it },
                onSave = { showProgressEditor = false }
            )
            Spacer(Modifier.height(16.dp))
        }

        // ── Friends ──
        FriendsCard()

        Spacer(Modifier.height(16.dp))

        // ── Reviews ──
        ReviewsSection()

        Spacer(Modifier.height(16.dp))

        // ── Metadata ──
        MetadataCard()

        Spacer(Modifier.height(32.dp))
    }
}

// ─── Estrellas ────────────────────────────────────────────────────────────────
@Composable
private fun StarRow(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val tint = when {
                index < rating.toInt() -> DesignGold
                index < rating         -> DesignGoldLight
                else                   -> Color(0xFFDDD8CE)
            }
            Icon(
                imageVector = if (index < rating.toInt()) Icons.Filled.Star
                else if (index < rating) Icons.Filled.StarHalf
                else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(6.dp))
        Text("$rating Rating", fontSize = 13.sp, color = DesignTextSec)
    }
}

// ─── Progress Card ────────────────────────────────────────────────────────────
@Composable
private fun ProgressCard(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    onSave: () -> Unit
) {
    val progress = currentPage.toFloat() / totalPages.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DesignNavy)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = DesignGold, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("UPDATE PROGRESS", color = DesignGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            }
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("$currentPage", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text(" of $totalPages pages", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                Spacer(Modifier.weight(1f))
                Text("${(progress * 100).toInt()}%", color = DesignGold, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = currentPage.toFloat(),
                onValueChange = { onPageChange(it.toInt()) },
                valueRange = 0f..totalPages.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = DesignGold,
                    activeTrackColor = DesignGold,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                )
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DesignNavyMid)
            ) { Text("Save Progress", fontWeight = FontWeight.SemiBold) }
        }
    }
}

// ─── Friends Card ─────────────────────────────────────────────────────────────
@Composable
private fun FriendsCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DesignCard),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("FRIENDS WHO READ THIS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DesignTextMuted, letterSpacing = 1.5.sp)
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(76.dp).height(32.dp)) {
                    listOf(Color(0xFF7B9EA6), Color(0xFFB87C6A), Color(0xFF8A7EB8))
                        .forEachIndexed { index, color ->
                            Box(
                                modifier = Modifier
                                    .offset(x = (index * 22).dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(2.dp, DesignCard, CircleShape)
                            )
                        }
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Marcus, Elena, and 12 others\nhave read this volume.",
                    fontSize = 13.sp,
                    color = DesignTextSec,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ─── Reviews Section ──────────────────────────────────────────────────────────
@Composable
private fun ReviewsSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Community Reviews", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DesignTextPrim)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {}) {
                Text("Write a Review", fontSize = 13.sp, color = DesignOrange)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.Edit, contentDescription = null, tint = DesignOrange, modifier = Modifier.size(13.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        sampleReviews.forEach { review ->
            ReviewCard(review)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ReviewCard(review: ReviewUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DesignCard),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(review.avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(review.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DesignTextPrim)
                    StarRow(rating = review.rating)
                }
                Text(review.timeAgo, fontSize = 12.sp, color = DesignTextMuted)
            }
            Spacer(Modifier.height(10.dp))
            Text(review.text, fontSize = 14.sp, color = DesignTextSec, lineHeight = 21.sp)
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = DesignDivider)
            Spacer(Modifier.height(10.dp))
            Row {
                Icon(Icons.Outlined.ThumbUp, contentDescription = null, tint = DesignTextMuted, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("${review.likes}", fontSize = 13.sp, color = DesignTextMuted)
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = DesignTextMuted, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("${review.comments}", fontSize = 13.sp, color = DesignTextMuted)
            }
        }
    }
}

// ─── Metadata Card ────────────────────────────────────────────────────────────
@Composable
private fun MetadataCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DesignCard),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("ACCESSION NO.", fontSize = 10.sp, color = DesignTextMuted, letterSpacing = 1.sp)
                Text("000452-FB", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DesignTextPrim)
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = DesignDivider)
            Spacer(Modifier.height(16.dp))
            MetadataRow("PUBLISHED", "April 10, 1925")
            Spacer(Modifier.height(12.dp))
            MetadataRow("PAGES", "218 Pages")
            Spacer(Modifier.height(12.dp))
            MetadataRow("LANGUAGE", "English (US)")
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = DesignDivider)
            Spacer(Modifier.height(16.dp))
            Text(
                "\"So we beat on, boats against the current, borne back ceaselessly into the past.\"",
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                color = DesignTextSec,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .border(1.5.dp, DesignGold.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("EX LIBRIS", fontSize = 9.sp, letterSpacing = 2.sp, color = DesignGold, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = DesignGold, modifier = Modifier.size(16.dp))
                    Text("FEEDBOOK", fontSize = 8.sp, letterSpacing = 1.5.sp, color = DesignGold, fontWeight = FontWeight.Bold)
                    Text("VERIFIED", fontSize = 7.sp, letterSpacing = 1.sp, color = DesignGold.copy(alpha = 0.7f))
                }
            }
        }
    }
}

@Composable
private fun MetadataRow(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = DesignOrange, letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 14.sp, color = DesignTextPrim, fontWeight = FontWeight.Medium)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
private fun BookDetailSuccessPreview() {
    FeedBookTheme(dynamicColor = false) {
        BookDetailContent(
            state = BookDetailState(
                book = Book(
                    id = "000452-FB",
                    title = "The Great Gatsby",
                    author = "F. Scott Fitzgerald",
                    description = "A story of the mysteriously wealthy Jay Gatsby."
                )
            ),
            onBackClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDetailLoadingPreview() {
    FeedBookTheme(dynamicColor = false) {
        BookDetailContent(
            state = BookDetailState(isLoading = true),
            onBackClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDetailErrorPreview() {
    FeedBookTheme(dynamicColor = false) {
        BookDetailContent(
            state = BookDetailState(error = "No se pudo cargar el libro."),
            onBackClick = {},
            onRetry = {}
        )
    }
}