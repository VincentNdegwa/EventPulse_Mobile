package com.example.eventpulse.Data.homeData

data class DashData(
    val day: String,
    val error: Boolean,
    val latestEvents: List<LatestEvent>,
    val recomendedEvents: List<RecomendedEvent>,
    val upCommingEvents: List<upCommingEvents>,
    val trendingEvents: List<trendingEvents>
)