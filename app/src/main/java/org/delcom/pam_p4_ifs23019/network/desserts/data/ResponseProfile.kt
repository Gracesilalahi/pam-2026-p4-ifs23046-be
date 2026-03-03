package org.delcom.pam_p4_ifs23019.network.desserts.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfile(
    val username: String,
    val nama: String,
    val tentang: String,
)