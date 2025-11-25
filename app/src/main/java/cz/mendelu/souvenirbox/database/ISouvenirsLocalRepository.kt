package cz.mendelu.souvenirbox.database

import kotlinx.coroutines.flow.Flow

interface ISouvenirsLocalRepository {
    fun getAll(): Flow<List<SouvenirEntity>>
}