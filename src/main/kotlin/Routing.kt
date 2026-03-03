package org.delcom

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.delcom.data.AppException
import org.delcom.data.ErrorResponse
import org.delcom.helpers.parseMessageToMap
import org.delcom.services.PlantService
import org.delcom.services.ProfileService
import org.delcom.services.BonekaService
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val plantService: PlantService by inject()
    val bonekaService: BonekaService by inject()
    val profileService: ProfileService by inject()

    install(StatusPages) {
        exception<AppException> { call, cause ->
            val dataMap: Map<String, List<String>> = parseMessageToMap(cause.message)
            call.respond(
                status = HttpStatusCode.fromValue(cause.code),
                message = ErrorResponse(
                    status = "fail",
                    message = if (dataMap.isEmpty()) cause.message else "Data tidak valid!",
                    data = if (dataMap.isEmpty()) null else dataMap.toString()
                )
            )
        }

        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ErrorResponse(
                    status = "error",
                    message = cause.message ?: "Unknown error",
                    data = ""
                )
            )
        }
    }

    routing {
        get("/") {
            call.respondText("API Ready. Dibuat oleh Anny Klaudya Hutabarat")
        }

        // --- Route Plants (Tetap Dipertahankan) ---
        route("/plants") {
            get { plantService.getAllPlants(call) }
            post { plantService.createPlant(call) }
            get("/{id}") { plantService.getPlantById(call) }
            put("/{id}") { plantService.updatePlant(call) }
            delete("/{id}") { plantService.deletePlant(call) }
            get("/{id}/image") { plantService.getPlantImage(call) }
        }

        // --- Route Boneka (Ganti dari Desserts) ---
        route("/boneka") {
            get { bonekaService.getAllBoneka(call) }
            post { bonekaService.createBoneka(call) }
            get("/{id}") { bonekaService.getBonekaById(call) }
            put("/{id}") { bonekaService.updateBoneka(call) }
            delete("/{id}") { bonekaService.deleteBoneka(call) }
            get("/{id}/image") { bonekaService.getBonekaImage(call) }
        }

        // --- Route Profile ---
        route("/profile"){
            get { profileService.getProfile(call) }
            get("/photo") { profileService.getProfilePhoto(call) }
        }
    }
}