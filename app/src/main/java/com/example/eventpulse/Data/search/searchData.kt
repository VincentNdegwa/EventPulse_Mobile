package com.example.eventpulse.Data.search

data class searchData(
    val `data`: List<SearchEvents>,
    val error: Boolean,
    val res: String
)