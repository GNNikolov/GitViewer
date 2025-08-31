package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
import com.gnnikolov.gitviewer.data.local.dao.UserDao
import com.gnnikolov.gitviewer.domain.IGitRepoRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
@HiltAndroidTest
class GitRepoModelsRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: IGitRepoRepository

    @Inject
    lateinit var dao: GitRepoDao

    @Inject
    lateinit var userDao: UserDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun getGitRepos_whenLocalCacheIsEmpty_fetchesRemoteDataAndCachesIt() = runTest {
        userDao.insert(FakeRemoteDataSource.fakeUser.toEntity())
        val remoteData = FakeRemoteDataSource.getFakeRemoteData()
        val isEmpty = dao.getAllForUser(FakeRemoteDataSource.fakeUser.id).isEmpty()
        Assert.assertTrue("Local data must be empty!", isEmpty)
        val result = repository.getReposForUser(FakeRemoteDataSource.fakeUser)
        Assert.assertNotNull(result)
        Assert.assertTrue("Items count does not match!", result!!.size == remoteData.size)
        result.forEachIndexed { idx, item ->
            Assert.assertTrue(
                "Item does n`t match with the remote one!",
                item == remoteData[idx]
            )
        }
    }
}