package cz.mendelu.souvenirbox.analyzers

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await

class ImageTagger {

    val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.65f)
            .build()
    )

    suspend fun generateTags(
        context: Context,
        imageUri: Uri
    ): List<String> {
        val image = InputImage.fromFilePath(context, imageUri)
        val labels = labeler.process(image).await()

        return labels
            .sortedByDescending { it.confidence }
            .take(5)
            .map { it.text.trim() }
            .filter { it.isNotBlank() }
            .distinct()
    }
}
