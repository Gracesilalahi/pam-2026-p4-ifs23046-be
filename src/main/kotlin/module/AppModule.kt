package org.delcom.module

import org.delcom.repositories.IPlantRepository
import org.delcom.repositories.IBonekaRepository
import org.delcom.repositories.PlantRepository
import org.delcom.repositories.BonekaRepository
import org.delcom.services.PlantService
import org.delcom.services.BonekaService
import org.delcom.services.ProfileService
import org.koin.dsl.module


val appModule = module {
    // Plant Repository
    single<IPlantRepository> {
        PlantRepository()
    }
    single<IBonekaRepository> {
        BonekaRepository()
    }

    // Plant Service
    single {
        PlantService(get())
    }
    single {
        BonekaService(get())
    }
    // Profile Service
    single {
        ProfileService()
    }
}