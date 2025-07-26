package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.data.database.GitRepoModelDao
import com.gnnikolov.gitviewer.data.repository.IGitRepoModelsRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class GitRepoModelsRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: IGitRepoModelsRepository

    @Inject
    lateinit var dao: GitRepoModelDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun getGitRepos_whenLocalCacheIsEmpty_fetchesRemoteDataAndCachesIt() = runTest {
        val isEmpty = dao.getAll().isEmpty()
        Assert.assertTrue("Local data must be empty!", isEmpty)
        val result = repository.getGitRepos()
        Assert.assertTrue("Items count does not match!", result.size == 3)
    }
}