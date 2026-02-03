package cz.mendelu.souvenirbox.analyzers

import android.content.Context
import android.net.Uri
import javax.inject.Inject

class MlKitImageTaggerFacade @Inject constructor(
    private val tagger: ImageTagger
) : IImageTagger {

    override suspend fun generateTags(context: Context, imageUri: Uri): List<String> {
        return tagger.generateTags(context, imageUri)
    }
}
