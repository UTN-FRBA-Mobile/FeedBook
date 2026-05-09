package com.example.feedbook.features.home.data.remote

import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend

class HomeRemoteDataSource(
    private val fakeBackend: FakeFeedBookBackend
) {
    fun observeHomeFeed() = fakeBackend.observeHomeFeed()
}
