package cz.mendelu.souvenirbox.analyzers

import android.content.Context
import android.net.Uri


interface IImageTagger {
    suspend fun generateTags(context: Context, imageUri: Uri): List<String>
}