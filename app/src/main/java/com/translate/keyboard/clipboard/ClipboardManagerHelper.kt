package com.translate.keyboard.clipboard

import android.content.Context
import android.content.SharedPreferences

data class ClipboardItem(
    val id: String,
    val text: String,
    val timestamp: Long,
    val isPinned: Boolean = false,
    val type: String = "text" // text, phone, email, url
)

class ClipboardManagerHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("clipboard_prefs", Context.MODE_PRIVATE)
    private val items = mutableListOf<ClipboardItem>()

    init {
        loadItems()
    }

    fun addClip(text: String) {
        if (text.isBlank() || isPasswordText(text)) return
        // Avoid duplicates
        items.removeAll { it.text == text && !it.isPinned }

        val type = detectType(text)
        val newItem = ClipboardItem(
            id = System.currentTimeMillis().toString(),
            text = text,
            timestamp = System.currentTimeMillis(),
            type = type
        )
        items.add(0, newItem)
        if (items.size > 50) {
            items.removeAt(items.size - 1)
        }
        saveItems()
    }

    private fun detectType(text: String): String {
        return when {
            android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches() -> "email"
            android.util.Patterns.PHONE.matcher(text).matches() -> "phone"
            android.util.Patterns.WEB_URL.matcher(text).matches() -> "url"
            else -> "text"
        }
    }

    private fun isPasswordText(text: String): Boolean {
        val lower = text.lowercase()
        return lower.contains("password") || lower.contains("pass") || text.length > 64
    }

    fun getItems(): List<ClipboardItem> = items

    fun togglePin(id: String) {
        val index = items.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = items[index]
            items[index] = item.copy(isPinned = !item.isPinned)
            saveItems()
        }
    }

    fun deleteItem(id: String) {
        items.removeAll { it.id == id && !it.isPinned }
        saveItems()
    }

    private fun saveItems() {
        val set = items.map { "${it.id}§${it.isPinned}§${it.timestamp}§${it.type}§${it.text}" }.toSet()
        prefs.edit().putStringSet("clip_set", set).apply()
    }

    private fun loadItems() {
        val set = prefs.getStringSet("clip_set", emptySet()) ?: emptySet()
        items.clear()
        for (str in set) {
            val parts = str.split("§", limit = 5)
            if (parts.size == 5) {
                items.add(
                    ClipboardItem(
                        id = parts[0],
                        isPinned = parts[1].toBoolean(),
                        timestamp = parts[2].toLong(),
                        type = parts[3],
                        text = parts[4]
                    )
                )
            }
        }
        items.sortByDescending { it.isPinned }
    }
}
