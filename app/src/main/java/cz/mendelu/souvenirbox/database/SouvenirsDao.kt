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
    fun getAllSouvenirs(): Flow<List<SouvenirEntity>>
    @Query("SELECT * FROM souvenirs WHERE id = :id")
    suspend fun getSouvenirById(id: Long): SouvenirEntity
    @Query("UPDATE souvenirs SET isFavourite = NOT isFavourite WHERE id = :id")
    suspend fun updateFavourite(id: Long)
    @Insert
    suspend fun createSouvenir(souvenir: SouvenirEntity): Long
    @Update
    suspend fun updateSouvenir(souvenir: SouvenirEntity): Int
    @Delete
    suspend fun deleteSouvenir(souvenir: SouvenirEntity)
}