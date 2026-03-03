package org.delcom.pam_p4_ifs23019.network.Desserts.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23019.helper.SuspendHelper
import org.delcom.pam_p4_ifs23019.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23019.network.Desserts.data.ResponseDessert
import org.delcom.pam_p4_ifs23019.network.Desserts.data.ResponseDessertAdd
import org.delcom.pam_p4_ifs23019.network.Desserts.data.ResponseDesserts
import org.delcom.pam_p4_ifs23019.network.Desserts.data.ResponseProfile

class DessertRepository (private val dessertApiService: DessertApiService): IDessertRepository {
    override suspend fun getProfile(): ResponseMessage<ResponseProfile?> {
        return SuspendHelper.safeApiCall {
            dessertApiService.getProfile()
        }
    }

    override suspend fun getAllDesserts(search: String?): ResponseMessage<ResponseDesserts?> {
        return SuspendHelper.safeApiCall {
            dessertApiService.getAllDesserts(search)
        }
    }

    override suspend fun postDessert(
        nama: RequestBody,
        deskripsi: RequestBody,
        bahanUtama: RequestBody,
        tingkatKemanisan: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseDessertAdd?> {
        return SuspendHelper.safeApiCall {
            dessertApiService.postDessert(
                nama = nama,
                deskripsi = deskripsi,
                bahanUtama = bahanUtama,
                tingkatKemanisan = tingkatKemanisan,
                harga = harga,
                file = file
            )
        }
    }

    override suspend fun getDessertById(DessertId: String): ResponseMessage<ResponseDessert?> {
        return SuspendHelper.safeApiCall {
            dessertApiService.getDessertById(DessertId)
        }
    }

    override suspend fun putDessert(
        DessertId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        bahanUtama: RequestBody,
        tingkatKemanisan: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            dessertApiService.putDessert(
                DessertId = DessertId,
                nama = nama,
                deskripsi = deskripsi,
                bahanUtama = bahanUtama,
                tingkatKemanisan = tingkatKemanisan,
                harga = harga,
                file = file
            )
        }
    }

    override suspend fun deleteDessert(DessertId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            dessertApiService.deleteDessert(DessertId)
        }
    }
}
