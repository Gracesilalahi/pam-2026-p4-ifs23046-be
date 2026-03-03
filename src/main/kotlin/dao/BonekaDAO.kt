package org.delcom.dao

import org.delcom.tables.BonekaTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import java.util.UUID

class BonekaDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, BonekaDAO>(BonekaTable)

    var nama by BonekaTable.nama
    var fotoBoneka by BonekaTable.pathGambar
    var deskripsi by BonekaTable.deskripsi
    var bahan by BonekaTable.bahan // Sebelumnya 'manfaat'
    var peringatanUmur by BonekaTable.peringatanUmur // Sebelumnya 'efekSamping'
    var createdAt by BonekaTable.createdAt
    var updatedAt by BonekaTable.updatedAt
}