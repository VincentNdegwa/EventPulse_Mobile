package com.example.eventpulse.Data.tickets

data class UserTickets(
    val `data`: List<TicketsData>,
    val error: Boolean,
    val message: String
)