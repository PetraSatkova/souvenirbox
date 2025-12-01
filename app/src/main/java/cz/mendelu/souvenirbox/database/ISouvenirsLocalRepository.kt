package cz.mendelu.souvenirbox.database

import kotlinx.coroutines.flow.Flow

interface ISouvenirsLocalRepository {
    fun getAllSouvenirs(): Flow<List<SouvenirEntity>>
    suspend fun getSouvenirById(id: Long): SouvenirEntity
    suspend fun getSouvenirsForYear(): Flow<List<SouvenirEntity>>
    suspend fun getRecentlyAddedSouvenirs(): Flow<List<SouvenirEntity>>
    suspend fun createSouvenir(souvenir: SouvenirEntity)
    suspend fun updateSouvenir(souvenir: SouvenirEntity)
    suspend fun deleteSouvenir(id: Long)
}