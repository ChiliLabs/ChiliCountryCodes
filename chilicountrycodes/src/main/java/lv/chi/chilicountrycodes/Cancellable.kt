package lv.chi.chilicountrycodes

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

interface Cancellable {
    fun cancel()
}

internal class JobCancellation(
    private val job: Job
) : Cancellable {

    init {
        job.start()
    }

    override fun cancel() {
        if (job.isActive) {
            job.cancel()
        }
    }
}

internal fun launchAsCancellable(dispatcher: CoroutineContext, block: suspend CoroutineScope.() -> Unit): Cancellable {
    val job = GlobalScope.launch(dispatcher, CoroutineStart.LAZY, block)
    return JobCancellation(job)
}

