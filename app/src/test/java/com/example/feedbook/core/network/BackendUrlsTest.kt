package com.example.feedbook.core.network

import org.junit.Assert.assertEquals
import org.junit.Test

class BackendUrlsTest {
    @Test
    fun `origin appends trailing slash when missing`() {
        assertEquals("http://localhost:8080/", BackendUrls.origin("http://localhost:8080"))
    }

    @Test
    fun `api base url appends api path once`() {
        assertEquals("http://localhost:8080/api/", BackendUrls.apiBaseUrl("http://localhost:8080/"))
    }
}
