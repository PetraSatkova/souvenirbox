package cz.mendelu.souvenirbox.fake

import android.content.Context
import android.net.Uri
import cz.mendelu.souvenirbox.analyzers.IImageTagger

class FakeImageTaggerFacade(
    private val tagsToReturn: List<String> = listOf("fake", "tag")
) : IImageTagger {
    override suspend fun generateTags(context: Context, imageUri: Uri): List<String> {
        return tagsToReturn
    }
}
