package com.gnnikolov.gitviewer.concurrency.test

import com.gnnikolov.gitviewer.concurrency.LockByKeyCache
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class LockByKeyCacheTest {

    private lateinit var lockCache: LockByKeyCache<String>

    @Before
    fun setUp() {
        lockCache = LockByKeyCache()
    }

    @Test
    fun `runLocked should execute producer and block on first run`() = runTest {
        val producerInvocations = AtomicInteger(0)
        val blockInvocations = AtomicInteger(0)
        val key = "testKey"
        val result = lockCache.getOrPut(key).runLocked(
            reset = false,
            onProduce = {
                producerInvocations.incrementAndGet()
                Result.success(Unit)
            },
            onExitLock = {
                blockInvocations.incrementAndGet()
                "Success"
            }
        )
        Assert.assertEquals(1, producerInvocations.get())
        Assert.assertEquals(1, blockInvocations.get())
        Assert.assertEquals("Success", result)
    }

    @Test
    fun `runLocked should NOT execute producer on second run if not forced`() = runTest {
        val producerInvocations = AtomicInteger(0)
        val key = "testKey"
        // First run to cache the state
        lockCache.getOrPut(key).runLocked(
            false,
            {
                producerInvocations.incrementAndGet()
                Result.success(Unit)
            },
            {}
        )
        // Second run
        lockCache.getOrPut(key).runLocked(
            false,
            { producerInvocations.incrementAndGet(); Result.success(Unit) },
            {}
        )
        Assert.assertEquals(1, producerInvocations.get()) // Should only be called once
    }

    @Test
    fun `runLocked should re-execute producer on second run if forced`() = runTest {
        val producerInvocations = AtomicInteger(0)
        val key = "testKey"
        // First run
        lockCache.getOrPut(key).runLocked(
            false,
            {
                producerInvocations.incrementAndGet()
                Result.success(Unit)
            },
            {}
        )
        // Second run with reset = true
        lockCache.getOrPut(key).runLocked(
            true,
            {
                producerInvocations.incrementAndGet()
                Result.success(Unit)
            },
            {}
        )
        Assert.assertEquals(2, producerInvocations.get())
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `parallel execution with same key should run producer only once`() = runTest {
        val producerInvocations = AtomicInteger(0)
        val blockInvocations = AtomicInteger(0)
        val key = "parallelKey"
        val coroutineCount = 7000
        val deferreds = (1..coroutineCount).map {
            async(context = Dispatchers.IO) {
                println("ThreadId:" + Thread.currentThread().id.toString())
                lockCache.getOrPut(key).runLocked<Unit, Unit>(
                    reset = false,
                    onProduce = {
                        delay(Random.nextLong(10, 130))
                        producerInvocations.incrementAndGet()
                        Result.success(Unit)
                    },
                    onExitLock = {
                        blockInvocations.incrementAndGet()
                    }
                )
            }
        }
        deferreds.awaitAll()
        Assert.assertEquals(1, producerInvocations.get())
        Assert.assertEquals(coroutineCount, blockInvocations.get())
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `parallel execution with different keys should run producers independently`() = runTest {
        val producerInvocations = AtomicInteger(0)
        val blockInvocations = AtomicInteger(0)
        val coroutineCount = 7000
        val deferreds = (1..coroutineCount).map { index ->
            async(context = Dispatchers.IO) {
                println("ThreadId:" + Thread.currentThread().id.toString())
                val key = "key-$index"
                lockCache.getOrPut(key).runLocked<Unit, Unit>(
                    reset = false,
                    onProduce = {
                        delay(Random.nextLong(10, 130))
                        producerInvocations.incrementAndGet()
                        Result.success(Unit)
                    },
                    onExitLock = {
                        blockInvocations.incrementAndGet()
                    }
                )
            }
        }
        deferreds.awaitAll()
        Assert.assertEquals(coroutineCount, producerInvocations.get())
        Assert.assertEquals(coroutineCount, blockInvocations.get())
    }

}