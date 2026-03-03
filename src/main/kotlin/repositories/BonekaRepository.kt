package org.delcom.repositories

import org.delcom.dao.BonekaDAO
import org.delcom.entities.Boneka
import org.delcom.helpers.daoToModel
import org.delcom.helpers.suspendTransaction
import org.delcom.tables.BonekaTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import java.util.UUID

class BonekaRepository : IBonekaRepository {
    override suspend fun getBoneka(search: String): List<Boneka> = suspendTransaction {
        if (search.isBlank()) {
            BonekaDAO.all()
                .orderBy(BonekaTable.createdAt to SortOrder.DESC)
                .limit(20)
                .map(::daoToModel)
        } else {
            val keyword = "%${search.lowercase()}%"

            BonekaDAO
                .find {
                    BonekaTable.nama.lowerCase() like keyword
                }
                .orderBy(BonekaTable.nama to SortOrder.ASC)
                .limit(20)
                .map(::daoToModel)
        }
    }

    override suspend fun getBonekaById(id: String): Boneka? = suspendTransaction {
        BonekaDAO
            .find { (BonekaTable.id eq UUID.fromString(id)) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun getBonekaByName(name: String): Boneka? = suspendTransaction {
        BonekaDAO
            .find { (BonekaTable.nama eq name) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addBoneka(boneka: Boneka): String = suspendTransaction {
        val bonekaDAO = BonekaDAO.new {
            nama = boneka.nama
            pathGambar = boneka.pathGambar
            deskripsi = boneka.deskripsi
            material = boneka.material  // Sinkron dengan properti Boneka
            ukuran = boneka.ukuran      // Sinkron dengan properti Boneka
            createdAt = boneka.createdAt
            updatedAt = boneka.updatedAt
        }

        bonekaDAO.id.value.toString()
    }

    override suspend fun updateBoneka(id: String, newBoneka: Boneka): Boolean = suspendTransaction {
        val bonekaDAO = BonekaDAO
            .find { BonekaTable.id eq UUID.fromString(id) }
            .limit(1)
            .firstOrNull()

        if (bonekaDAO != null) {
            bonekaDAO.nama = newBoneka.nama
            bonekaDAO.pathGambar = newBoneka.pathGambar
            bonekaDAO.deskripsi = newBoneka.deskripsi
            bonekaDAO.material = newBoneka.material
            bonekaDAO.ukuran = newBoneka.ukuran
            bonekaDAO.updatedAt = newBoneka.updatedAt
            true
        } else {
            false
        }
    }

    override suspend fun removeBoneka(id: String): Boolean = suspendTransaction {
        val rowsDeleted = BonekaTable.deleteWhere {
            BonekaTable.id eq UUID.fromString(id)
        }
        rowsDeleted == 1
    }
}