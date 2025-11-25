package cz.mendelu.souvenirbox.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "souvenirs")
data class SouvenirEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var name: String,
    var latitude: Double,
    var longitude: Double,
    var price: Double,
    var currency: String,
    var date: Long,
    var notes: String
)