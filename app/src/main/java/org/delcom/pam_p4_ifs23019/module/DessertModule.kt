package org.delcom.pam_p4_ifs23019.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23019.network.desserts.service.IDessertAppContainer
import org.delcom.pam_p4_ifs23019.network.desserts.service.IDessertRepository
import org.delcom.pam_p4_ifs23019.network.desserts.service.DessertAppContainer

@Module
@InstallIn(SingletonComponent::class)
object DessertModule {
    @Provides
    fun provideDessertContainer(): IDessertAppContainer {
        return DessertAppContainer()
    }

    @Provides
    fun provideDessertRepository(container: IDessertAppContainer): IDessertRepository {
        return container.DessertRepository
    }
}
