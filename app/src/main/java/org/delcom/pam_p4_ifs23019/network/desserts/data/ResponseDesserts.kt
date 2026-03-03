package org.delcom.pam_p4_ifs23019.network.desserts.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDesserts (
    val Desserts: List<ResponseDessertData>
)

@Serializable
data class ResponseDessert (
    val Dessert: ResponseDessertData
)

@Serializable
data class ResponseDessertAdd (
    val DessertId: String
)

@Serializable
data class ResponseDessertData(
    val id: String,
    val nama: String,
    val deskripsi: String,
    val bahanUtama: String,
    val tingkatKemanisan: String,
    val harga: String,
    val createdAt: String,
    val updatedAt: String
)