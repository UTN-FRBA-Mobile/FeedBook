package com.example.feedbook.features.profile.data.mapper

import com.example.feedbook.features.profile.data.remote.dto.*
import com.example.feedbook.features.profile.domain.model.*

fun ProfileDto.toDomain(): ReaderProfile = ReaderProfile(
    name = name,
    handle = handle,
    quote = quote,
    avatar = avatar.toDomain(),
    readingGoal = readingGoal?.toDomain(),
    readingStreak = readingStreak.toDomain(),
    currentBook = currentBook.toDomain(),
    upNextBooks = upNextBooks.map(QueuedBookDto::toDomain),
    completedBooks = completedBooks,
    profileStats = profileStats.map(ProfileStatDto::toDomain),
    publicLibrary = publicLibrary.map(LibraryBookDto::toDomain),
    featuredReviews = featuredReviews.map(FeaturedReviewDto::toDomain)
)

fun UpdateProfileCommand.toDto(): UpdateProfileRequestDto = UpdateProfileRequestDto(
    name = name,
    handle = handle,
    quote = quote,
    avatarTopColorHex = avatarTopColorHex,
    avatarBottomColorHex = avatarBottomColorHex,
    avatarImageUri = avatarImageUri,
    targetPagesPerDay = targetPagesPerDay
)

private fun AvatarDto.toDomain(): AvatarInfo = AvatarInfo(topColorHex, bottomColorHex, imageUri)
private fun ReadingGoalDto.toDomain(): ReadingGoal = ReadingGoal(targetPagesPerDay, currentAveragePagesPerDay)
private fun ReadingStreakDto.toDomain(): ReadingStreak = ReadingStreak(days, week.map(StreakDayDto::toDomain))
private fun StreakDayDto.toDomain(): StreakDay = StreakDay(label, fillFraction, isToday, completed)
private fun CurrentBookDto.toDomain(): CurrentBook = CurrentBook(title, author, page, totalPages, progress, coverAccentHex)
private fun QueuedBookDto.toDomain(): QueuedBook = QueuedBook(title, author)
private fun ProfileStatDto.toDomain(): ProfileStat = ProfileStat(label, value)
private fun LibraryBookDto.toDomain(): LibraryBook = LibraryBook(title, accentHex)
private fun FeaturedReviewDto.toDomain(): FeaturedReview = FeaturedReview(bookTitle, rating, timeAgo, excerpt, accentHex)
