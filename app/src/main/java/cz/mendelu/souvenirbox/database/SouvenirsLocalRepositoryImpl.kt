package cz.mendelu.souvenirbox.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SouvenirsLocalRepositoryImpl @Inject constructor(
    private var souvenirsDao: SouvenirsDao
) : ISouvenirsLocalRepository {

    override fun getAll(): Flow<List<SouvenirEntity>> {
        return souvenirsDao.getAll()
    }

}