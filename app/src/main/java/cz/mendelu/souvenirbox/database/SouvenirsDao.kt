package cz.mendelu.souvenirbox.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SouvenirsDao {
    @Query("SELECT * FROM souvenirs")
    fun getAll(): Flow<List<SouvenirEntity>>
}