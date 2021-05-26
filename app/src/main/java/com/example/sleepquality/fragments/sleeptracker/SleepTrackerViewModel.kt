package com.example.sleepquality.fragments.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sleepquality.database.SleepDatabaseDao

class SleepTrackerViewModel (val database: SleepDatabaseDao,
    application: Application): AndroidViewModel(application) {

}