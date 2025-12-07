package cz.mendelu.souvenirbox.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "souvenirs")
data class SouvenirEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var name: String,
    var latitude: Double,
    var longitude: Double,
    var city: String,
    var price: Double,
    var currency: String,
    var date: Long,
    var notes: String,
    var imageUri: String?
)