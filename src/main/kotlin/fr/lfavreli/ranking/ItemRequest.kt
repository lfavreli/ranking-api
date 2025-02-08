package fr.lfavreli.ranking

import kotlinx.serialization.Serializable

@Serializable
data class ItemRequest(val id: String, val data: String)
