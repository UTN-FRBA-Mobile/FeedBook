package com.example.feedbook.features.library.data.remote

import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend

class LibraryRemoteDataSource(
    private val fakeBackend: FakeFeedBookBackend
) {
    fun observeOwnLibrary() = fakeBackend.observeOwnLibrary()
}
