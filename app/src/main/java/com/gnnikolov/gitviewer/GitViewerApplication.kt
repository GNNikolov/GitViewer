package com.gnnikolov.gitviewer

import android.app.Application
import com.gnnikolov.gitviewer.di.ApplicationComponent
import com.gnnikolov.gitviewer.di.DaggerApplicationComponent

class GitViewerApplication : Application() {
    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }
}