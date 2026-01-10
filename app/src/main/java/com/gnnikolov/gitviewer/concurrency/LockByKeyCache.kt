package com.gnnikolov.gitviewer.concurrency

import androidx.annotation.GuardedBy
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class LockByKeyCache<Key : Any> @Inject constructor() {

    class CacheState {

        private val mutex = Mutex()

        @GuardedBy("mutex")
        private var isCached: Boolean = false

        suspend fun <T, ProduceResult> runLocked(
            reset: Boolean,
            onProduce: suspend () -> Result<ProduceResult>,
            onExitLock: suspend () -> T
        ): T? {
            return mutex.withLock {
                if (reset) {
                    isCached = false
                }
                if (!isCached) {
                    val result = onProduce()
                    if (result.isSuccess) {
                        isCached = true
                    }
                }
                onExitLock()
            }
        }
    }

    private val cache = ConcurrentHashMap<Key, CacheState>()

    fun getOrPut(key: Key): CacheState = cache.computeIfAbsent(key) { CacheState() }

}