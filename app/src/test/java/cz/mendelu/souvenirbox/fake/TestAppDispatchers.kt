package cz.mendelu.souvenirbox.fake

import cz.mendelu.souvenirbox.dispatcher.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher

class TestAppDispatchers(
    override val io: CoroutineDispatcher,
    override val main: CoroutineDispatcher
) : AppDispatchers
