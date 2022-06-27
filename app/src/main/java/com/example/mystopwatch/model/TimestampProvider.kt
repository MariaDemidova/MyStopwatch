package com.example.mystopwatch.model

interface TimestampProvider {
    fun getMilliseconds(): Long
}