package cz.mendelu.souvenirbox.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable

@Entity(tableName = "souvenirs")
data class SouvenirEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var name: String,
    var latitude: Double,
    var longitude: Double,
    var city: String,
    var country: String,
    var price: Double,
    var currency: String,
    var date: Long,
    var isFavourite: Boolean,
    var notes: String,
    var imageUri: String?,
    var tags: List<String>?
): ClusterItem, Serializable {

    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String {
        return name
    }

    override fun getZIndex(): Float {
        return 0.0f
    }

}
