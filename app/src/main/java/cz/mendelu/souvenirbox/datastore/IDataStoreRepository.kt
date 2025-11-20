package cz.mendelu.souvenirbox.datastore

interface IDataStoreRepository {
    suspend fun setLoginSuccessful()
    suspend fun getLoginSuccessful(): Boolean
}