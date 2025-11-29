package cz.mendelu.souvenirbox.utils

import java.util.*

object LanguageUtils {

    private val SLOVAK = "sk"
    private val ENGLISH = "en"

    fun isLanguageSlovak(): Boolean {
        val language = Locale.getDefault().language
        return language.equals(SLOVAK)
    }
}