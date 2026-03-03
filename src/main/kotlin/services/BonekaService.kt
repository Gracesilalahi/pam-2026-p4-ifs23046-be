package org.delcom.services

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.delcom.data.AppException
import org.delcom.data.DataResponse
import org.delcom.data.BonekaRequest
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.IBonekaRepository
import java.io.File
import java.util.*

class BonekaService(private val bonekaRepository: IBonekaRepository) {

    // Mengambil semua data boneka
    suspend fun getAllBoneka(call: ApplicationCall) {
        val search = call.request.queryParameters["search"] ?: ""

        val listBoneka = bonekaRepository.getBoneka(search)

        val response = DataResponse(
            "success",
            "Berhasil mengambil daftar koleksi boneka",
            mapOf(Pair("boneka", listBoneka))
        )
        call.respond(response)
    }

    // Mengambil data boneka berdasarkan id
    suspend fun getBonekaById(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID boneka tidak boleh kosong!")

        val boneka = bonekaRepository.getBonekaById(id)
            ?: throw AppException(404, "Data boneka tidak tersedia!")

        val response = DataResponse(
            "success",
            "Berhasil mengambil detail boneka",
            mapOf(Pair("boneka", boneka))
        )
        call.respond(response)
    }

    // Ambil data request dari form multipart
    private suspend fun getBonekaRequest(call: ApplicationCall): BonekaRequest {
        val bonekaReq = BonekaRequest()

        val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 5)
        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "nama" -> bonekaReq.nama = part.value.trim()
                        "deskripsi" -> bonekaReq.deskripsi = part.value
                        "material" -> bonekaReq.material = part.value
                        "ukuran" -> bonekaReq.ukuran = part.value
                    }
                }

                is PartData.FileItem -> {
                    val ext = part.originalFileName
                        ?.substringAfterLast('.', "")
                        ?.let { if (it.isNotEmpty()) ".$it" else "" }
                        ?: ""

                    val fileName = UUID.randomUUID().toString() + ext
                    val filePath = "uploads/boneka/$fileName"

                    val file = File(filePath)
                    file.parentFile.mkdirs()

                    part.provider().copyAndClose(file.writeChannel())
                    bonekaReq.pathGambar = filePath
                }

                else -> {}
            }
            part.dispose()
        }

        return bonekaReq
    }

    // Validasi request data
    private fun validateBonekaRequest(bonekaReq: BonekaRequest){
        val validatorHelper = ValidatorHelper(bonekaReq.toMap())
        validatorHelper.required("nama", "Nama boneka tidak boleh kosong")
        validatorHelper.required("deskripsi", "Deskripsi tidak boleh kosong")
        validatorHelper.required("material", "Material/Bahan tidak boleh kosong")
        validatorHelper.required("ukuran", "Ukuran tidak boleh kosong")
        validatorHelper.required("pathGambar", "Foto boneka tidak boleh kosong")
        validatorHelper.validate()

        val file = File(bonekaReq.pathGambar)
        if (!file.exists()) {
            throw AppException(400, "Gambar boneka gagal diupload!")
        }
    }

    // Menambahkan data boneka baru
    suspend fun createBoneka(call: ApplicationCall) {
        val bonekaReq = getBonekaRequest(call)
        validateBonekaRequest(bonekaReq)

        val existBoneka = bonekaRepository.getBonekaByName(bonekaReq.nama)
        if(existBoneka != null){
            val tmpFile = File(bonekaReq.pathGambar)
            if(tmpFile.exists()) tmpFile.delete()
            throw AppException(409, "Boneka dengan nama ini sudah ada di koleksi!")
        }

        val bonekaId = bonekaRepository.addBoneka(bonekaReq.toEntity())

        val response = DataResponse(
            "success",
            "Berhasil menambahkan boneka baru ke koleksi",
            mapOf(Pair("bonekaId", bonekaId))
        )
        call.respond(response)
    }

    // Mengubah data boneka
    suspend fun updateBoneka(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID boneka tidak boleh kosong!")

        val oldBoneka = bonekaRepository.getBonekaById(id)
            ?: throw AppException(404, "Data boneka tidak tersedia!")

        val bonekaReq = getBonekaRequest(call)

        if(bonekaReq.pathGambar.isEmpty()){
            bonekaReq.pathGambar = oldBoneka.pathGambar
        }

        validateBonekaRequest(bonekaReq)

        if(bonekaReq.nama != oldBoneka.nama){
            val existBoneka = bonekaRepository.getBonekaByName(bonekaReq.nama)
            if(existBoneka != null){
                val tmpFile = File(bonekaReq.pathGambar)
                if(tmpFile.exists()) tmpFile.delete()
                throw AppException(409, "Nama boneka ini sudah digunakan!")
            }
        }

        if(bonekaReq.pathGambar != oldBoneka.pathGambar){
            val oldFile = File(oldBoneka.pathGambar)
            if(oldFile.exists()){
                oldFile.delete()
            }
        }

        val isUpdated = bonekaRepository.updateBoneka(id, bonekaReq.toEntity())
        if (!isUpdated) throw AppException(400, "Gagal memperbarui data boneka!")

        call.respond(DataResponse("success", "Berhasil memperbarui info boneka", null))
    }

    // Menghapus data boneka
    suspend fun deleteBoneka(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID boneka tidak boleh kosong!")

        val oldBoneka = bonekaRepository.getBonekaById(id)
            ?: throw AppException(404, "Data boneka tidak ditemukan!")
        val oldFile = File(oldBoneka.pathGambar)

        val isDeleted = bonekaRepository.removeBoneka(id)
        if (!isDeleted) throw AppException(400, "Gagal menghapus boneka!")

        if (oldFile.exists()) oldFile.delete()

        call.respond(DataResponse("success", "Boneka berhasil dihapus dari koleksi", null))
    }

    // Mengambil file gambar boneka
    suspend fun getBonekaImage(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: return call.respond(HttpStatusCode.BadRequest)

        val boneka = bonekaRepository.getBonekaById(id)
            ?: return call.respond(HttpStatusCode.NotFound)

        val file = File(boneka.pathGambar)
        if (!file.exists()) return call.respond(HttpStatusCode.NotFound)

        call.respondFile(file)
    }
}