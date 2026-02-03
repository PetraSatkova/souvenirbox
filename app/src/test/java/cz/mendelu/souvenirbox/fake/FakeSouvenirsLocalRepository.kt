package cz.mendelu.souvenirbox.fake

import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import cz.mendelu.souvenirbox.database.SouvenirEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSouvenirsLocalRepository(
    initial: List<SouvenirEntity> = emptyList(),
    private val souvenirById: SouvenirEntity? = null
) : ISouvenirsLocalRepository {

    private val flow = MutableStateFlow(initial)
    var deleted: SouvenirEntity? = null
    var updatedFavouriteId: Long? = null

    override fun getAllSouvenirs(): Flow<List<SouvenirEntity>> = flow.asStateFlow()

    fun setSouvenirs(list: List<SouvenirEntity>) {
        flow.value = list
    }

    // Not needed for DashboardViewModel tests:
    override suspend fun getSouvenirById(id: Long): SouvenirEntity {
        return souvenirById ?: error("Not used in DashboardViewModel test")
    }

    override suspend fun createSouvenir(souvenir: SouvenirEntity): Long =
        error("Not used in test")

    override suspend fun updateSouvenir(souvenir: SouvenirEntity): Int =
        error("Not used in test")

    override suspend fun updateFavourite(id: Long) {
        updatedFavouriteId = id
    }

    override suspend fun deleteSouvenir(souvenir: SouvenirEntity) {
        deleted = souvenir
    }
}
