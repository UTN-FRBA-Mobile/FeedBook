package com.example.feedbook.features.home.data.mapper

import com.example.feedbook.features.home.data.remote.dto.HomeCuratorDto
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import com.example.feedbook.features.home.data.remote.dto.HomeRankedBookDto
import com.example.feedbook.features.home.data.remote.dto.HomeReadingRoomDto
import com.example.feedbook.features.home.domain.model.HomeAvatar
import com.example.feedbook.features.home.domain.model.HomeCurator
import com.example.feedbook.features.home.domain.model.HomeFeed
import com.example.feedbook.features.home.domain.model.HomeFeaturedBook
import com.example.feedbook.features.home.domain.model.HomeRankedBook
import com.example.feedbook.features.home.domain.model.HomeReadingRoom

fun HomeDto.toDomain(): HomeFeed = HomeFeed(
    trendingTitle = trendingTitle,
    avatar = HomeAvatar(
        topColorHex = avatar.topColorHex,
        bottomColorHex = avatar.bottomColorHex,
        imageUri = avatar.imageUri
    ),
    featuredBook = HomeFeaturedBook(
        bookId = featuredBook.bookId,
        label = featuredBook.label,
        title = featuredBook.title,
        author = featuredBook.author,
        coverImageUrl = featuredBook.coverImageUrl
    ),
    rankedBooks = rankedBooks.map(HomeRankedBookDto::toDomain),
    readingRooms = readingRooms.map(HomeReadingRoomDto::toDomain),
    curators = curators.map(HomeCuratorDto::toDomain)
)

private fun HomeRankedBookDto.toDomain(): HomeRankedBook = HomeRankedBook(
    bookId = bookId,
    rankLabel = rankLabel,
    title = title,
    author = author,
    coverImageUrl = coverImageUrl
)

private fun HomeReadingRoomDto.toDomain(): HomeReadingRoom = HomeReadingRoom(
    id = id,
    hostName = hostName,
    hostImageUrl = hostImageUrl,
    title = title,
    shortDescription = shortDescription,
    readerCountLabel = readerCountLabel,
    memberCount = memberCount,
    isFollowed = isFollowed,
    isAdult = isAdult
)

private fun HomeCuratorDto.toDomain(): HomeCurator = HomeCurator(
    name = name,
    focus = focus,
    imageUrl = imageUrl
)
