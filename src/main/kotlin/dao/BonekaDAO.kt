package org.delcom.dao

import org.delcom.tables.BonekaTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import java.util.UUID

class BonekaDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, BonekaDAO>(BonekaTable)

    var nama by BonekaTable.nama
    var pathGambar by BonekaTable.pathGambar // Sesuaikan agar seragam dengan Entity
    var deskripsi by BonekaTable.deskripsi
    var material by BonekaTable.material    // Menggunakan 'material' (sebelumnya 'manfaat')
    var ukuran by BonekaTable.ukuran        // Menggunakan 'ukuran' (sebelumnya 'efekSamping')
    var createdAt by BonekaTable.createdAt
    var updatedAt by BonekaTable.updatedAt
}