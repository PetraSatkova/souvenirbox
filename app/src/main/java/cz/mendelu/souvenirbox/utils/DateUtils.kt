package cz.mendelu.souvenirbox.utils

import androidx.compose.ui.text.intl.Locale
import java.text.SimpleDateFormat
import java.util.Calendar

class DateUtils {

    companion object {
        private val DATE_FORMAT_SK = "dd. MM. yyyy"
        private val DATE_FORMAT_EN = "yyyy/MM/dd"

        fun getDateString(unixTime: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = unixTime

            val format: SimpleDateFormat
            if (LanguageUtils.isLanguageSlovak()) {
                format = SimpleDateFormat(DATE_FORMAT_SK)
            } else {
                format = SimpleDateFormat(DATE_FORMAT_EN)
            }
            return format.format(calendar.getTime())
        }
    }
}