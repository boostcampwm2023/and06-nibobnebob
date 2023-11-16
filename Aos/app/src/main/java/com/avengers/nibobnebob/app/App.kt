package com.avengers.nibobnebob.app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    companion object{
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "nibobnebob")
    }
}