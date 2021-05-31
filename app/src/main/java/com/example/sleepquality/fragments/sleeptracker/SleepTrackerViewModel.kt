package com.example.sleepquality.fragments.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.sleepquality.database.SleepDatabaseDao
import com.example.sleepquality.database.SleepNight
import com.example.sleepquality.formatNights
import kotlinx.coroutines.launch

class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var tonight = MutableLiveData<SleepNight?>()
    private val nights = database.getAllNights()
    val nightsString = Transformations.map(nights){ nights ->
        formatNights(nights, application.resources)
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        var night = database.getTonight()
        if (night?.endTime != night?.startTime) {
            night = null
        }
        return night
    }

    fun onStartTracking() {
//continue and update the UI.
        viewModelScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    fun onStopeTracking(){
        viewModelScope.launch{
            val oldNight= tonight.value?: return@launch
            oldNight.endTime = System.currentTimeMillis()
            update(oldNight)
        }
    }

    private suspend fun insert(night: SleepNight){
        database.insert(night)
    }

    private suspend fun update(night: SleepNight){
        database.update(night)
    }


}