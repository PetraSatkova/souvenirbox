package cz.mendelu.souvenirbox.dispatcher

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DefaultAppDispatchers @Inject constructor() : AppDispatchers {
    override val io = Dispatchers.IO
    override val main = Dispatchers.Main
}