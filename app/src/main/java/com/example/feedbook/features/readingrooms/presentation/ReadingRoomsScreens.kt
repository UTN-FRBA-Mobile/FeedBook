package com.example.feedbook.features.readingrooms.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.RemoteBookCover
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomCommentDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomDetailDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomFeedItemDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomMemberDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomPeriodDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomRatingDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomSummaryDto
import com.example.feedbook.features.profile.presentation.ProfileVariant
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileAvatarArtwork
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
private fun ReadingRoomChrome(
    title: String,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit
) {
    FeedBookScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        variant = ProfileVariant.OWN,
        activeTab = BottomBarTab.FEED,
        avatarStyle = defaultAvatarStyle(),
        showBottomBar = false
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ProfileColors.Background)
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.common_back),
                        tint = ProfileColors.PrimaryText
                    )
                }
                Text(
                    text = title,
                    color = ProfileColors.PrimaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }
    }
}

@Composable
fun ReadingRoomListScreen(
    state: ReadingRoomListState,
    onBackClick: () -> Unit,
    onRoomClick: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onCreateRoom: (String, String, String, Boolean) -> Unit
) {
    var showCreate by remember { mutableStateOf(false) }
    ReadingRoomChrome(
        title = stringResource(R.string.reading_rooms_title),
        onBackClick = onBackClick
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.reading_rooms_title),
                        style = ProfileTypography.HeroName.copy(fontSize = 32.sp, lineHeight = 36.sp),
                        color = ProfileColors.PrimaryText,
                        modifier = Modifier.weight(1f)
                    )
                    FilledIconButton(
                        onClick = { showCreate = true },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = stringResource(R.string.reading_rooms_create), tint = ProfileColors.Accent)
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = onQueryChange,
                    label = { Text(stringResource(R.string.reading_rooms_search_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = readableTextFieldColors()
                )
            }
            if (state.isLoading) {
                item { Text(stringResource(R.string.reading_rooms_loading), color = ProfileColors.PrimaryText) }
            }
            state.error?.let { error ->
                item { Text(error, color = ProfileColors.Accent) }
            }
            val query = state.query.trim().lowercase()
            val followed = state.rooms?.followed.orEmpty().filter { it.name.lowercase().contains(query) }
            val other = state.rooms?.other.orEmpty().filter { it.name.lowercase().contains(query) }
            item { SectionTitle(stringResource(R.string.library_followed_authors_title)) }
            if (followed.isEmpty()) {
                item { EmptyText(stringResource(R.string.reading_rooms_followed_empty)) }
            } else {
                items(followed) { room -> RoomSummaryCard(room, onRoomClick) }
            }
            item { SectionTitle(stringResource(R.string.reading_rooms_other_title)) }
            if (other.isEmpty()) {
                item { EmptyText(stringResource(R.string.reading_rooms_other_empty)) }
            } else {
                items(other) { room -> RoomSummaryCard(room, onRoomClick) }
            }
        }
    }
    if (showCreate) {
        CreateRoomDialog(
            onDismiss = { showCreate = false },
            onCreate = { name, description, shortDescription, isAdult ->
                showCreate = false
                onCreateRoom(name, description, shortDescription, isAdult)
            }
        )
    }
}

@Composable
fun ReadingRoomScreen(
    state: ReadingRoomState,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    onJoinClick: () -> Unit,
    onChangeBook: (String) -> Unit,
    onRate: (Float) -> Unit,
    onComment: (String, String?) -> Unit
) {
    var showBookPicker by remember { mutableStateOf(false) }
    var showRatings by remember { mutableStateOf<List<ReadingRoomRatingDto>?>(null) }
    val room = state.room
    ReadingRoomChrome(
        title = room?.name ?: stringResource(R.string.reading_room_info_title),
        onBackClick = onBackClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading) {
                Text(stringResource(R.string.reading_room_loading), color = ProfileColors.PrimaryText)
            } else if (room == null) {
                Text(state.error ?: stringResource(R.string.reading_room_loading_error), color = ProfileColors.PrimaryText)
            } else {
                RoomHeader(room, onInfoClick, onJoinClick)
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ActiveBookPanel(
                            room = room,
                            onRatingsClick = { ratings -> showRatings = ratings },
                            onRate = onRate,
                            onComment = onComment
                        )
                    }
                    item {
                        if (room.isMember) {
                            Button(onClick = { showBookPicker = true }, modifier = Modifier.fillMaxWidth()) {
                                Text(stringResource(R.string.reading_room_change_active_book))
                            }
                        }
                    }
                    item { SectionTitle(stringResource(R.string.library_read_history_title)) }
                    if (room.history.isEmpty()) {
                        item { EmptyText(stringResource(R.string.reading_room_no_history)) }
                    } else {
                        items(room.history) { period -> HistoricPeriodCard(period) }
                    }
                    state.feedback?.let { item { Text(it, color = ProfileColors.Accent) } }
                }
            }
        }
    }
    if (showBookPicker && room != null) {
        BookPickerDialog(
            books = state.followedBooks,
            currentBookId = room.activeBook?.id,
            onDismiss = { showBookPicker = false },
            onSelect = {
                showBookPicker = false
                onChangeBook(it)
            }
        )
    }
    showRatings?.let { ratings ->
        RatingsDialog(ratings = ratings, onDismiss = { showRatings = null })
    }
}

@Composable
fun ReadingRoomInfoScreen(
    state: ReadingRoomState,
    onBackClick: () -> Unit,
    onSaveDescription: (String) -> Unit,
    onKick: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    val room = state.room
    var editing by remember { mutableStateOf(false) }
    var description by remember(room?.description) { mutableStateOf(room?.description.orEmpty()) }
    var deleteName by remember { mutableStateOf("") }
    ReadingRoomChrome(
        title = stringResource(R.string.reading_room_info_title),
        onBackClick = onBackClick
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (room != null) {
                item {
                    Text(
                        stringResource(R.string.reading_room_info_title),
                        style = ProfileTypography.HeroName.copy(fontSize = 28.sp),
                        color = ProfileColors.PrimaryText
                    )
                }
                item {
                    ProfileSurfaceCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(room.name, style = ProfileTypography.HeroName.copy(fontSize = 30.sp), color = ProfileColors.PrimaryText)
                            Text(stringResource(R.string.reading_room_integrants_count, room.memberCount), color = ProfileColors.SecondaryText)
                            if (editing) {
                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 4,
                                    colors = readableTextFieldColors()
                                )
                                Button(onClick = { editing = false; onSaveDescription(description) }) { Text(stringResource(R.string.common_save)) }
                            } else {
                                Text(room.description, color = ProfileColors.PrimaryText)
                                if (room.creatorId == "me") {
                                    OutlinedButton(onClick = { editing = true }) {
                                        Icon(Icons.Outlined.Edit, contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text(stringResource(R.string.edit_profile_title))
                                    }
                                }
                            }
                        }
                    }
                }
                item { SectionTitle(stringResource(R.string.reading_room_members)) }
                items(room.members) { member ->
                    MemberRow(member = member, canKick = room.creatorId == "me" && !member.isAdmin, onKick = onKick)
                }
                if (room.creatorId == "me") {
                    item {
                        ProfileSurfaceCard(
                            containerColor = Color(0xFF3A2323),
                            borderColor = Color(0xFF7A4D4A)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(stringResource(R.string.reading_room_delete_title), color = Color(0xFFFFB4A9), fontWeight = FontWeight.Bold)
                                OutlinedTextField(
                                    value = deleteName,
                                    onValueChange = { deleteName = it },
                                    label = { Text(room.name) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = destructiveTextFieldColors()
                                )
                                Button(onClick = { onDelete(deleteName) }) {
                                    Icon(Icons.Outlined.Delete, contentDescription = null, tint = ProfileColors.PrimaryText)
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.reading_room_delete_confirm), color = ProfileColors.PrimaryText)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoomHeader(room: ReadingRoomDetailDto, onInfoClick: () -> Unit, onJoinClick: () -> Unit) {
    ProfileSurfaceCard(onClick = onInfoClick) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(room.creatorAvatarUrl, contentDescription = room.creatorName, modifier = Modifier.size(56.dp).clip(CircleShape))
            Column(modifier = Modifier.weight(1f)) {
                Text(room.name, style = ProfileTypography.HeroName.copy(fontSize = 30.sp, lineHeight = 34.sp), color = ProfileColors.PrimaryText)
                Text(stringResource(R.string.reading_room_integrants_count, room.memberCount), color = ProfileColors.SecondaryText)
            }
            if (!room.isMember) {
                Button(onClick = onJoinClick) { Text(stringResource(R.string.reading_room_follow)) }
            }
        }
    }
}

@Composable
private fun ActiveBookPanel(room: ReadingRoomDetailDto, onRatingsClick: (List<ReadingRoomRatingDto>) -> Unit, onRate: (Float) -> Unit, onComment: (String, String?) -> Unit) {
    val active = room.activeBook
    val activePeriod = room.activePeriod
    ProfileSurfaceCard {
        if (active == null) {
            EmptyText(
                if (room.isMember) {
                    stringResource(R.string.reading_room_no_active_book_with_hint)
                } else {
                    stringResource(R.string.reading_room_no_active_book)
                }
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    RemoteBookCover(active.title, active.coverImageUrl, modifier = Modifier.size(width = 92.dp, height = 132.dp).clip(RoundedCornerShape(4.dp)), fallbackBackground = ProfileColors.AccentSoft)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(active.title, style = ProfileTypography.HeroName.copy(fontSize = 28.sp), color = ProfileColors.PrimaryText)
                        Text(active.author, color = ProfileColors.SecondaryText)
                        Text(stringResource(R.string.reading_room_active_since, room.activeSince.orEmpty().take(10)), color = ProfileColors.SecondaryText)
                        Text(
                            stringResource(R.string.reading_room_rating_average, activePeriod?.averageRating ?: 0f),
                            color = ProfileColors.Accent,
                            modifier = Modifier.clickable { onRatingsClick(activePeriod?.ratings.orEmpty()) }
                        )
                    }
                }
                if (room.isMember) {
                    RatingInput(onRate)
                    CommentInput(onSend = { onComment(it, null) })
                }
                FeedList(room.feed, isHistoric = false, canReply = room.isMember, onReply = onComment)
            }
        }
    }
}

@Composable
private fun HistoricPeriodCard(period: ReadingRoomPeriodDto) {
    ProfileSurfaceCard(containerColor = Color(0xFFF2EFE9), borderColor = Color(0xFFD6D0C6)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(period.book.title, style = ProfileTypography.HeroName.copy(fontSize = 24.sp), color = ProfileColors.PrimaryText)
            Text(stringResource(R.string.reading_room_rating_period, period.startedAt.take(10), period.endedAt?.take(10).orEmpty()), color = ProfileColors.SecondaryText)
            Text(stringResource(R.string.reading_room_rating_average, period.averageRating), color = ProfileColors.SecondaryText)
            FeedList(period.feed, isHistoric = true, canReply = false, onReply = { _, _ -> })
        }
    }
}

@Composable
private fun FeedList(items: List<ReadingRoomFeedItemDto>, isHistoric: Boolean, canReply: Boolean, onReply: (String, String?) -> Unit) {
    if (items.isEmpty()) {
        EmptyText(stringResource(R.string.reading_room_no_comments))
    }
    items.forEach { item ->
        item.event?.let {
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(if (isHistoric) Color(0xFFE0DAD0) else ProfileColors.AccentSoft).padding(10.dp)) {
                Text(it.text, color = ProfileColors.PrimaryText, fontSize = 14.sp)
            }
        }
        item.comment?.let { CommentCard(it, isHistoric, canReply, allowReply = canReply, onReply = onReply) }
    }
}

@Composable
private fun CommentCard(
    comment: ReadingRoomCommentDto,
    isHistoric: Boolean,
    canReply: Boolean,
    allowReply: Boolean,
    onReply: (String, String?) -> Unit
) {
    var reply by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ProfileAvatarArtwork(
            avatarStyle = defaultAvatarStyle(),
            avatarPreset = null,
            avatarImageUri = comment.avatarUrl,
            modifier = Modifier.size(34.dp),
            imageShape = CircleShape
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(comment.userName, color = ProfileColors.PrimaryText, fontWeight = FontWeight.Bold)
            Text(comment.text, color = if (isHistoric) ProfileColors.SecondaryText else ProfileColors.PrimaryText)
            if (allowReply) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = reply,
                        onValueChange = { reply = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.reading_room_reply_hint)) },
                        colors = readableTextFieldColors()
                    )
                    IconButton(onClick = { if (reply.isNotBlank()) { onReply(reply, comment.id); reply = "" } }) {
                        Icon(Icons.Outlined.Send, contentDescription = stringResource(R.string.reading_room_send_reply), tint = ProfileColors.Accent)
                    }
                }
            }
            comment.replies.forEach { child ->
                Box(modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                    CommentCard(child, isHistoric, canReply = false, allowReply = false, onReply = onReply)
                }
            }
        }
    }
}

@Composable
private fun RoomSummaryCard(room: ReadingRoomSummaryDto, onRoomClick: (String) -> Unit) {
    ProfileSurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onRoomClick(room.id) },
        containerColor = ProfileColors.Surface
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(room.creatorAvatarUrl, contentDescription = room.creatorName, modifier = Modifier.size(52.dp).clip(CircleShape))
            Column(modifier = Modifier.weight(1f)) {
                Text(room.name, style = ProfileTypography.HeroName.copy(fontSize = 24.sp), color = ProfileColors.PrimaryText, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(room.shortDescription, color = ProfileColors.SecondaryText, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.PeopleOutline, contentDescription = null, tint = ProfileColors.SecondaryText)
                Text(room.memberCount.toString(), color = ProfileColors.SecondaryText)
            }
        }
    }
}

@Composable
private fun RatingInput(onRate: (Float) -> Unit) {
    var rating by remember { mutableFloatStateOf(4f) }
    Column {
        Text(stringResource(R.string.reading_room_rating_label, rating), color = ProfileColors.PrimaryText)
        Slider(value = rating, onValueChange = { rating = it }, valueRange = 0f..5f, steps = 9)
        OutlinedButton(onClick = { onRate(rating) }) { Text(stringResource(R.string.reading_room_save_rating)) }
    }
}

@Composable
private fun CommentInput(onSend: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(R.string.reading_room_add_comment)) },
            colors = readableTextFieldColors()
        )
        IconButton(onClick = { if (text.isNotBlank()) { onSend(text); text = "" } }) {
            Icon(Icons.Outlined.Send, contentDescription = stringResource(R.string.reading_room_send_comment), tint = ProfileColors.Accent)
        }
    }
}

@Composable
private fun BookPickerDialog(books: List<BookDto>, currentBookId: String?, onDismiss: () -> Unit, onSelect: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = books.filter { it.title.contains(query, true) || it.author.contains(query, true) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF8F5EF),
        titleContentColor = ProfileColors.PrimaryText,
        textContentColor = ProfileColors.PrimaryText,
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.reading_room_close), color = ProfileColors.Accent) } },
        title = { Text(stringResource(R.string.reading_room_change_active_book), color = ProfileColors.PrimaryText) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text(stringResource(R.string.reading_room_filter_by_title_author)) },
                        colors = readableTextFieldColors()
                    )
                }
                if (filtered.isEmpty()) item { EmptyText(stringResource(R.string.reading_room_no_followed_books)) }
                items(filtered) { book ->
                    TextButton(onClick = { if (book.id != currentBookId) onSelect(book.id) }, enabled = book.id != currentBookId) {
                        Text(
                            text = "${book.title} - ${book.author}",
                            color = if (book.id == currentBookId) ProfileColors.SecondaryText else ProfileColors.PrimaryText
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun RatingsDialog(ratings: List<ReadingRoomRatingDto>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF8F5EF),
        titleContentColor = ProfileColors.PrimaryText,
        textContentColor = ProfileColors.PrimaryText,
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.reading_room_close), color = ProfileColors.Accent) } },
        title = { Text(stringResource(R.string.reading_room_group_ratings), color = ProfileColors.PrimaryText) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (ratings.isEmpty()) {
                    EmptyText(stringResource(R.string.reading_room_no_ratings))
                }
                ratings.forEach { rating ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = rating.avatarUrl,
                            contentDescription = rating.userName,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(rating.userName, color = ProfileColors.PrimaryText, fontWeight = FontWeight.Medium)
                            Text(starRatingText(rating.rating), color = Color(0xFF8A5A13), fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun CreateRoomDialog(onDismiss: () -> Unit, onCreate: (String, String, String, Boolean) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var shortDescription by remember { mutableStateOf("") }
    var isAdult by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF8F5EF),
        titleContentColor = ProfileColors.PrimaryText,
        textContentColor = ProfileColors.PrimaryText,
        confirmButton = {
            Button(onClick = { onCreate(name, description, shortDescription, isAdult) }, enabled = name.isNotBlank() && description.isNotBlank() && shortDescription.isNotBlank()) {
            Text(stringResource(R.string.common_save))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_cancel), color = ProfileColors.Accent) } },
        title = { Text(stringResource(R.string.reading_room_new_title), color = ProfileColors.PrimaryText) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text(stringResource(R.string.reading_room_name_label)) }, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences), colors = readableTextFieldColors())
                OutlinedTextField(shortDescription, { shortDescription = it }, label = { Text(stringResource(R.string.reading_room_short_description_label)) }, colors = readableTextFieldColors())
                OutlinedTextField(description, { description = it }, label = { Text(stringResource(R.string.reading_room_description_label)) }, minLines = 3, colors = readableTextFieldColors())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(isAdult, onCheckedChange = { isAdult = it })
                    Text("+18", color = ProfileColors.PrimaryText)
                }
            }
        }
    )
}

@Composable
private fun MemberRow(member: ReadingRoomMemberDto, canKick: Boolean, onKick: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(member.avatarUrl, contentDescription = member.name, modifier = Modifier.size(42.dp).clip(CircleShape))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(member.name, color = ProfileColors.PrimaryText, fontWeight = if (member.isAdmin) FontWeight.Bold else FontWeight.Normal)
            if (member.isAdmin) Text(stringResource(R.string.reading_room_creator_label), color = ProfileColors.Accent)
        }
        if (canKick) TextButton(onClick = { onKick(member.userId) }) { Text(stringResource(R.string.reading_room_kick)) }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = ProfileTypography.HeroName.copy(fontSize = 24.sp), color = ProfileColors.PrimaryText)
}

@Composable
private fun EmptyText(text: String) {
    Text(text, color = ProfileColors.SecondaryText)
}

private fun starRatingText(rating: Float): String {
    val filled = rating.toInt().coerceIn(0, 5)
    return buildString {
        repeat(filled) { append('★') }
        repeat(5 - filled) { append('☆') }
    }
}

@Composable
private fun readableTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = ProfileColors.PrimaryText,
    unfocusedTextColor = ProfileColors.PrimaryText,
    disabledTextColor = ProfileColors.SecondaryText,
    focusedLabelColor = ProfileColors.PrimaryText,
    unfocusedLabelColor = ProfileColors.SecondaryText,
    focusedPlaceholderColor = ProfileColors.SecondaryText,
    unfocusedPlaceholderColor = ProfileColors.SecondaryText,
    cursorColor = ProfileColors.Accent,
    focusedBorderColor = ProfileColors.Accent,
    unfocusedBorderColor = ProfileColors.Border
)

@Composable
private fun destructiveTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFFFFF7F2),
    unfocusedTextColor = Color(0xFFFFF7F2),
    disabledTextColor = Color(0xFFE5C7C0),
    focusedLabelColor = Color(0xFFFFB4A9),
    unfocusedLabelColor = Color(0xFFE7BBB4),
    focusedPlaceholderColor = Color(0xFFE7BBB4),
    unfocusedPlaceholderColor = Color(0xFFE7BBB4),
    cursorColor = Color(0xFFFFC6B8),
    focusedBorderColor = Color(0xFFC98256),
    unfocusedBorderColor = Color(0xFF8E5C45)
)
