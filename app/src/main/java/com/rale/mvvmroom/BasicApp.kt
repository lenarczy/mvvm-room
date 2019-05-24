package com.rale.mvvmroom

import android.app.Application
import com.rale.mvvmroom.db.AppDatabase
import timber.log.Timber

class BasicApp: Application() {

    private var appExecutors by DelegatesExt.notNullSingleValue<AppExecutors>()
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        appExecutors = AppExecutors()
    }

    fun getRepository() = DataRepository.getInstance(getDatabase())

    private fun getDatabase() = AppDatabase.getInstance(applicationContext, appExecutors)
}