package com.example.eventpulse.Data.tickets

data class TicketsData(
    val created_at: String,
    val event: Event,
    val event_agenda: String,
    val event_application_id: Int,
    val event_id: Int,
    val expectation: String,
    val learning_objective: Any,
    val similar_event: String,
    val status: String,
    val suggestions: Any,
    val updated_at: String,
    val user_id: Int
)