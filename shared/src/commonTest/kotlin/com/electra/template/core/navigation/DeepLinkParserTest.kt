package com.electra.template.core.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeepLinkParserTest {
    @Test
    fun parsesTodoListRoot() {
        assertEquals(Destination.TodoList, DeepLinkParser.parse("template://todos"))
    }

    @Test
    fun parsesTodoDetailWithId() {
        assertEquals(Destination.TodoDetail("42"), DeepLinkParser.parse("template://todos/42"))
    }

    @Test
    fun parsesTodoDetailNewWhenNoId() {
        assertEquals(Destination.TodoDetail(null), DeepLinkParser.parse("template://todos/new"))
    }

    @Test
    fun returnsNullForUnknown() {
        assertNull(DeepLinkParser.parse("template://whatever"))
    }
}
