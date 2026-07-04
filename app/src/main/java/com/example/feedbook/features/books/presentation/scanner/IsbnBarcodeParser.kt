package com.example.feedbook.features.books.presentation.scanner

import com.example.feedbook.features.books.domain.model.Book

internal fun extractIsbnFromBarcode(rawValue: String?): String? {
    val value = rawValue?.filter { it.isDigit() || it == 'X' || it == 'x' }?.uppercase().orEmpty()
    return when {
        value.length == 13 && value.all(Char::isDigit) && value.startsWithBooklandPrefix() &&
            value.hasValidIsbn13Checksum() -> value

        value.length == 10 && value.hasValidIsbn10Checksum() -> value
        else -> null
    }
}

internal fun findBookIdByIsbn(books: List<Book>, scannedIsbn: String): String? {
    val scannedCandidates = scannedIsbn.isbnLookupCandidates()
    return books.firstOrNull { book ->
        book.isbn.isbnLookupCandidates().any { candidate -> candidate in scannedCandidates }
    }?.id
}

private fun String.isbnLookupCandidates(): Set<String> {
    val normalized = filter { it.isDigit() || it == 'X' || it == 'x' }.uppercase()
    return buildSet {
        if (normalized.length == 13 && normalized.hasValidIsbn13Checksum()) {
            add(normalized)
            normalized.toIsbn10()?.let(::add)
        }
        if (normalized.length == 10 && normalized.hasValidIsbn10Checksum()) {
            add(normalized)
            normalized.toIsbn13()?.let(::add)
        }
    }
}

private fun String.startsWithBooklandPrefix(): Boolean = startsWith("978") || startsWith("979")

private fun String.hasValidIsbn13Checksum(): Boolean {
    if (length != 13 || !all(Char::isDigit)) return false
    val sum = take(12).mapIndexed { index, char ->
        char.digitToInt() * if (index % 2 == 0) 1 else 3
    }.sum()
    val checkDigit = (10 - (sum % 10)) % 10
    return checkDigit == last().digitToInt()
}

private fun String.hasValidIsbn10Checksum(): Boolean {
    if (length != 10) return false
    val sum = mapIndexed { index, char ->
        val value = when {
            char == 'X' && index == 9 -> 10
            char.isDigit() -> char.digitToInt()
            else -> return false
        }
        value * (10 - index)
    }.sum()
    return sum % 11 == 0
}

private fun String.toIsbn10(): String? {
    if (!startsWith("978") || !hasValidIsbn13Checksum()) return null
    val base = substring(3, 12)
    val sum = base.mapIndexed { index, char -> char.digitToInt() * (10 - index) }.sum()
    val checkValue = (11 - (sum % 11)) % 11
    val checkChar = if (checkValue == 10) "X" else checkValue.toString()
    return base + checkChar
}

private fun String.toIsbn13(): String? {
    if (!hasValidIsbn10Checksum()) return null
    val base = "978" + take(9)
    val sum = base.mapIndexed { index, char ->
        char.digitToInt() * if (index % 2 == 0) 1 else 3
    }.sum()
    val checkDigit = (10 - (sum % 10)) % 10
    return base + checkDigit
}
