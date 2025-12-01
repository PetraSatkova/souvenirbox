package cz.mendelu.souvenirbox.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SouvenirsDao {
    @Query("SELECT * FROM souvenirs")
    fun getAllSouvenirs(): Flow<List<SouvenirEntity>>
    @Query("SELECT * FROM souvenirs WHERE id = :id")
    fun getSouvenirById(id: Long): SouvenirEntity
    @Query(
        "SELECT * FROM souvenirs " +
                "WHERE date >= :startOfYear AND date < :startOfNextYear"
    )
    fun getSouvenirsForYear(
        startOfYear: Long,
        startOfNextYear: Long
    ): Flow<List<SouvenirEntity>>
    @Query("SELECT * FROM souvenirs")
    fun getRecentlyAddedSouvenirs(): Flow<List<SouvenirEntity>>
    @Query("SELECT * FROM souvenirs")
    fun createSouvenir(souvenir: SouvenirEntity)
    @Query("SELECT * FROM souvenirs")
    fun updateSouvenir(souvenir: SouvenirEntity)
    @Query("SELECT * FROM souvenirs")
    fun deleteSouvenir(id: Long)
}