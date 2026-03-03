package org.delcom.pam_p4_ifs23019.network.desserts.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23019.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23019.network.desserts.data.ResponseDessert
import org.delcom.pam_p4_ifs23019.network.desserts.data.ResponseDessertAdd
import org.delcom.pam_p4_ifs23019.network.desserts.data.ResponseDesserts
import org.delcom.pam_p4_ifs23019.network.desserts.data.ResponseProfile

interface IDessertRepository {
    // Ambil profile developer
    suspend fun getProfile(): ResponseMessage<ResponseProfile?>

    // Ambil semua data dessert
    suspend fun getAllDesserts(
        search: String? = null
    ): ResponseMessage<ResponseDesserts?>

    // Tambah data dessert
    suspend fun postDessert(
        nama: RequestBody,
        deskripsi: RequestBody,
        bahanUtama: RequestBody,
        tingkatKemanisan: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseDessertAdd?>

    // Ambil data dessert berdasarkan ID
    suspend fun getDessertById(
        DessertId: String
    ): ResponseMessage<ResponseDessert?>


    // Ubah data dessert
    suspend fun putDessert(
        DessertId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        bahanUtama: RequestBody,
        tingkatKemanisan: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data dessert
    suspend fun deleteDessert(
        DessertId: String
    ): ResponseMessage<String?>
}
