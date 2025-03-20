package com.bowleu.probcalc.util

import androidx.compose.runtime.mutableStateListOf

class LimitedList<T>(val maxSize: Int) {
    private val _items = mutableStateListOf<T>()
    val size
        get() = _items.size
    val items
        get() = _items.toList()

    init {
        require(maxSize >= 0) { "List size cannot be less than 0." }
    }

    fun add(item: T) {
        if (size == maxSize) {
            drop(0)
        }
        _items.add(item)
    }

    fun drop(position: Int) {
        require(position in 0..<size) { "Position out of bounds." }
        _items.removeAt(position)
    }

    fun get(position: Int): T? {
        return _items.getOrNull(position)
    }

    fun clear() {
        _items.clear()
    }
}