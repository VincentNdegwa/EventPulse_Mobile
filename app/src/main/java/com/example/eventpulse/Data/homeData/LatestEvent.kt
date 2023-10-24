package com.example.eventpulse.Data.homeData

data class LatestEvent(
    val address: String,
    val age_limit: String,
    val category: String,
    val created_at: String,
    val creator_id: Int,
    val deadline_application: String,
    val description: String,
    val event_date: String,
    val event_image: String,
    val hosts: List<Host>,
    val id: Int,
    val meeting_link: String,
    val price: String,
    val title: String,
    val updated_at: String,
    val venue: String
)