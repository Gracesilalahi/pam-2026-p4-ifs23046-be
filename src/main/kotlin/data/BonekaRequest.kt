package org.delcom.data

import kotlinx.serialization.Serializable
import org.delcom.entities.Boneka

@Serializable
data class BonekaRequest(
    var nama: String = "",
    var deskripsi: String = "",
    var material: String = "", // Menggantikan 'bahanUtama' (misal: Bulu Rasfur, Dakron)
    var ukuran: String = "",   // Menggantikan 'kategori' (misal: Kecil, Sedang, Jumbo)
    var pathGambar: String = "",
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nama" to nama,
            "deskripsi" to deskripsi,
            "material" to material,
            "ukuran" to ukuran,
            "pathGambar" to pathGambar
        )
    }

    fun toEntity(): Boneka {
        return Boneka(
            nama = nama,
            deskripsi = deskripsi,
            material = material,
            ukuran = ukuran,
            pathGambar = pathGambar,
        )
    }
}