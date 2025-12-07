package cz.mendelu.souvenirbox.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SouvenirsDao {
    @Query("SELECT * FROM souvenirs")
    suspend fun getAllSouvenirs(): List<SouvenirEntity>
    @Query("SELECT * FROM souvenirs WHERE id = :id")
    suspend fun getSouvenirById(id: Long): SouvenirEntity
    @Insert
    suspend fun createSouvenir(souvenir: SouvenirEntity): Long
    @Update
    suspend fun updateSouvenir(souvenir: SouvenirEntity)
    @Delete
    suspend fun deleteSouvenir(souvenir: SouvenirEntity)
}