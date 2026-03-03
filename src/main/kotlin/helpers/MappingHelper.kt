package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.BonekaDAO // Tambahkan import ini
import org.delcom.dao.PlantDAO
import org.delcom.entities.Boneka // Tambahkan import ini
import org.delcom.entities.Plant
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)


fun daoToModel(dao: PlantDAO) = Plant(
    dao.id.value.toString(),
    dao.nama,
    dao.pathGambar,
    dao.deskripsi,
    dao.manfaat,
    dao.efekSamping,
    dao.createdAt,
    dao.updatedAt
)

fun daoToModel(dao: BonekaDAO) = Boneka(
    id = dao.id.value.toString(),
    nama = dao.nama,
    pathGambar = dao.fotoBoneka, // Sesuai dengan nama properti di BonekaDAO
    deskripsi = dao.deskripsi,
    material = dao.bahan,        // Mapping dari 'bahan' di DAO ke 'material' di Entity
    ukuran = dao.peringatanUmur, // Mapping dari 'peringatanUmur' atau 'ukuran' di DAO
    createdAt = dao.createdAt,
    updatedAt = dao.updatedAt
)