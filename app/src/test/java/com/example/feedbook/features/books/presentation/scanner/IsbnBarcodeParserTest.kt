package com.example.feedbook.features.books.presentation.scanner

import com.example.feedbook.features.books.domain.model.Book
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class IsbnBarcodeParserTest {
    @Test
    fun `extracts valid bookland ISBN13 barcode`() {
        assertEquals("9781400031702", extractIsbnFromBarcode("9781400031702"))
    }

    @Test
    fun `rejects non book EAN13 barcode`() {
        assertNull(extractIsbnFromBarcode("7790895000997"))
    }

    @Test
    fun `matches scanned ISBN13 against catalog ISBN10`() {
        val books = listOf(
            book(id = "1", isbn = "1400031702"),
            book(id = "2", isbn = "9780156001311")
        )

        assertEquals("1", findBookIdByIsbn(books, "9781400031702"))
    }

    @Test
    fun `returns null when scanned ISBN is not in catalog`() {
        val books = listOf(book(id = "1", isbn = "9781400031702"))

        assertNull(findBookIdByIsbn(books, "9780156001311"))
    }

    private fun book(id: String, isbn: String): Book {
        return Book(
            id = id,
            title = "Title $id",
            author = "Author",
            description = "Description",
            coverImageUrl = null,
            language = "English",
            genre = "Fiction",
            pages = 100,
            published = "2026",
            isbn = isbn
        )
    }
}
