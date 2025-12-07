package cz.mendelu.souvenirbox.database

interface ISouvenirsLocalRepository {
    suspend fun getAllSouvenirs(): List<SouvenirEntity>
    suspend fun getSouvenirById(id: Long): SouvenirEntity
    suspend fun createSouvenir(souvenir: SouvenirEntity): Long
    suspend fun updateSouvenir(souvenir: SouvenirEntity)
    suspend fun deleteSouvenir(souvenir: SouvenirEntity)
}