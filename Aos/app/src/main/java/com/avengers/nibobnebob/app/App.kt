package com.avengers.nibobnebob.app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.avengers.nibobnebob.BuildConfig
import com.avengers.nibobnebob.presentation.util.Constants.APP_NAME
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        initializeNaverSDK()
    }

    companion object{
        lateinit var instance : App
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name =APP_NAME)
        fun getContext(): Context = instance.applicationContext
    }

    private fun initializeNaverSDK(){
        NaverIdLoginSDK.initialize(
            applicationContext,
            BuildConfig.NAVER_LOGIN_CLIENT_ID,
            BuildConfig.NAVER_LOGIN_CLIENT_SECRET,
            APP_NAME)
        NaverIdLoginSDK.showDevelopersLog(true)
    }
}