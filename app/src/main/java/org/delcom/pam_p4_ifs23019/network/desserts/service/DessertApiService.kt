package org.delcom.pam_p4_ifs23019.network.desserts.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23019.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23019.network.desserts.data.ResponseDessert
import org.delcom.pam_p4_ifs23019.network.desserts.data.ResponseDessertAdd
import org.delcom.pam_p4_ifs23019.network.desserts.data.ResponseDesserts
import org.delcom.pam_p4_ifs23019.network.desserts.data.ResponseProfile
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface DessertApiService {
    // Ambil profile developer
    @GET("profile")
    suspend fun getProfile(): ResponseMessage<ResponseProfile?>

    // Ambil semua data dessert
    @GET("Desserts")
    suspend fun getAllDesserts(
        @Query("search") search: String? = null
    ): ResponseMessage<ResponseDesserts?>

    // Tambah data dessert
    @Multipart
    @POST("/Desserts")
    suspend fun postDessert(
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("bahanUtama") bahanUtama: RequestBody,
        @Part("tingkatKemanisan") tingkatKemanisan: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part file: MultipartBody.Part
    ): ResponseMessage<ResponseDessertAdd?>

    // Ambil data dessert berdasarkan ID
    @GET("Desserts/{DessertId}")
    suspend fun getDessertById(
        @Path("DessertId") DessertId: String
    ): ResponseMessage<ResponseDessert?>


    // Ubah data dessert
    @Multipart
    @PUT("/Desserts/{DessertId}")
    suspend fun putDessert(
        @Path("DessertId") DessertId: String,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("bahanUtama") bahanUtama: RequestBody,
        @Part("tingkatKemanisan") tingkatKemanisan: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data dessert
    @DELETE("Desserts/{DessertId}")
    suspend fun deleteDessert(
        @Path("DessertId") DessertId: String
    ): ResponseMessage<String?>
}
