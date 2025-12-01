package cz.mendelu.souvenirbox.database

import cz.mendelu.souvenirbox.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SouvenirsLocalRepositoryImpl @Inject constructor(
    private var souvenirsDao: SouvenirsDao
) : ISouvenirsLocalRepository {

    override fun getAllSouvenirs(): Flow<List<SouvenirEntity>> {
        return souvenirsDao.getAllSouvenirs()
    }

    override suspend fun getSouvenirById(id: Long): SouvenirEntity {
        return souvenirsDao.getSouvenirById(id)
    }

    override suspend fun getSouvenirsForYear(): Flow<List<SouvenirEntity>> {
        return souvenirsDao.getSouvenirsForYear(
            startOfYear = DateUtils.getYearBounds(2025).first,
            startOfNextYear = DateUtils.getYearBounds(2025).second
        )
    }

    override suspend fun getRecentlyAddedSouvenirs(): Flow<List<SouvenirEntity>> {
        return souvenirsDao.getRecentlyAddedSouvenirs()
    }

    override suspend fun createSouvenir(souvenir: SouvenirEntity) {
        return souvenirsDao.createSouvenir(souvenir)
    }

    override suspend fun updateSouvenir(souvenir: SouvenirEntity) {
        return souvenirsDao.updateSouvenir(souvenir)
    }

    override suspend fun deleteSouvenir(id: Long) {
        return souvenirsDao.deleteSouvenir(id)

    }

}