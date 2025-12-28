package cz.mendelu.souvenirbox.database

import kotlinx.coroutines.flow.Flow

interface ISouvenirsLocalRepository {
    fun getAllSouvenirs(): Flow<List<SouvenirEntity>>
    suspend fun getSouvenirById(id: Long): SouvenirEntity
    suspend fun createSouvenir(souvenir: SouvenirEntity): Long
    suspend fun updateSouvenir(souvenir: SouvenirEntity)
    suspend fun updateFavourite(id: Long)
    suspend fun deleteSouvenir(souvenir: SouvenirEntity)
}