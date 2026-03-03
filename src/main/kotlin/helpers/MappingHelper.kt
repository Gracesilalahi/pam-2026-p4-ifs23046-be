package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.BonekaDAO
import org.delcom.dao.PlantDAO
import org.delcom.entities.Boneka
import org.delcom.entities.Plant
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

// Helper untuk menjalankan transaksi database secara asynchronous
suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

// Mapper untuk Tumbuhan (Tetap dipertahankan)
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

// Mapper untuk Boneka (Disesuaikan dengan BonekaDAO terbaru)
fun daoToModel(dao: BonekaDAO) = Boneka(
    id = dao.id.value.toString(),
    nama = dao.nama,
    pathGambar = dao.pathGambar, // Menggunakan pathGambar agar seragam
    deskripsi = dao.deskripsi,
    material = dao.material,     // Menggunakan material (bukan bahan lagi)
    ukuran = dao.ukuran,         // Menggunakan ukuran (bukan peringatanUmur lagi)
    createdAt = dao.createdAt,
    updatedAt = dao.updatedAt
)