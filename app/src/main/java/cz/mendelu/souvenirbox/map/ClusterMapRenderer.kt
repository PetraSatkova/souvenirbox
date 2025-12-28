package cz.mendelu.souvenirbox.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import cz.mendelu.souvenirbox.database.SouvenirEntity

class ClusterMapRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<SouvenirEntity>
) : DefaultClusterRenderer<SouvenirEntity>(context, map, clusterManager)
{
    override fun shouldRenderAsCluster(cluster: Cluster<SouvenirEntity?>): Boolean {
        return cluster.size > 2
    }

}