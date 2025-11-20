package cz.mendelu.souvenirbox.communication

data class CommunicationError(
    val code: Int,
    val message: String? = null
)
