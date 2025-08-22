package com.gnnikolov.gitviewer.concurrency

import androidx.annotation.GuardedBy
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class LockByKeyCache<Key : Any> @Inject constructor() {

    class CacheState {

        private val mutex = Mutex()

        @GuardedBy("mutex")
        private var isCached: Boolean = false

        suspend fun <T, ProduceResult> runLocked(
            reset: Boolean,
            produce: suspend () -> Result<ProduceResult>,
            block: suspend () -> T
        ): T? {
            return mutex.withLock {
                if (reset) {
                    isCached = false
                }
                if (!isCached) {
                    val result = produce()
                    if (result.isSuccess) {
                        isCached = true
                    }
                }
                block()
            }
        }
    }

    private val cacheLock = Mutex()

    //TODO: Supporting emptying of cache!!!
    @GuardedBy("cacheLock")
    private val cache = HashMap<Key, CacheState>()

    suspend fun getState(key: Key) =
        cacheLock.withLock { cache.getOrPut(key) { CacheState() } }


}