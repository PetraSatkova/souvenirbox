package cz.mendelu.souvenirbox.database

import javax.inject.Inject

class SouvenirsLocalRepositoryImpl @Inject constructor(
    private var souvenirsDao: SouvenirsDao
) : ISouvenirsLocalRepository {

    override suspend fun getAllSouvenirs(): List<SouvenirEntity> {
        return souvenirsDao.getAllSouvenirs()
    }

    override suspend fun getSouvenirById(id: Long): SouvenirEntity {
        return souvenirsDao.getSouvenirById(id)
    }

    override suspend fun createSouvenir(souvenir: SouvenirEntity): Long {
        return souvenirsDao.createSouvenir(souvenir)
    }

    override suspend fun updateSouvenir(souvenir: SouvenirEntity) {
        return souvenirsDao.updateSouvenir(souvenir)
    }

    override suspend fun deleteSouvenir(souvenir: SouvenirEntity) {
        return souvenirsDao.deleteSouvenir(souvenir)

    }

}