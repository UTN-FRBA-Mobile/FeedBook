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

    @Test
    fun `private backend origins bind to wifi`() {
        assertEquals(true, BackendUrls.shouldBindToWifi("http://192.168.4.2:8080/"))
        assertEquals(true, BackendUrls.shouldBindToWifi("http://10.42.0.1:8080/"))
        assertEquals(true, BackendUrls.shouldBindToWifi("http://172.16.0.5:8080/"))
    }

    @Test
    fun `localhost and emulator origins do not bind to wifi`() {
        assertEquals(false, BackendUrls.shouldBindToWifi("http://localhost:8080/"))
        assertEquals(false, BackendUrls.shouldBindToWifi("http://10.0.2.2:8080/"))
    }
}
