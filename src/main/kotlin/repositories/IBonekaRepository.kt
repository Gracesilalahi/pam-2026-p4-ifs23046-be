package org.delcom.repositories

import org.delcom.entities.Boneka

interface IBonekaRepository {
    suspend fun getBoneka(search: String): List<Boneka>
    suspend fun getBonekaById(id: String): Boneka?
    suspend fun getBonekaByName(name: String): Boneka?
    suspend fun addBoneka(boneka: Boneka): String
    suspend fun updateBoneka(id: String, newBoneka: Boneka): Boolean
    suspend fun removeBoneka(id: String): Boolean
}