package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object BonekaTable : UUIDTable("boneka") {
    val nama = varchar("nama", 100)
    val pathGambar = varchar("path_gambar", 255)
    val deskripsi = text("deskripsi")
    val material = text("material") // Menggantikan 'bahanUtama'
    val ukuran = varchar("ukuran", 50) // Menggantikan 'kategori'
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}