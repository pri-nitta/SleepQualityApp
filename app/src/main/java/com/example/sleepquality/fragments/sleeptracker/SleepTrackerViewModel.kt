package com.example.sleepquality.fragments.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import com.example.sleepquality.database.SleepDatabaseDao
import com.example.sleepquality.database.SleepNight
import com.example.sleepquality.formatNights
import kotlinx.coroutines.launch

class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var tonight = MutableLiveData<SleepNight?>()
    val nights = database.getAllNights()
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    private var _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    private suspend fun getTonightFromDatabase(): SleepNight? {
        var night = database.getTonight()
        if (night?.endTime != night?.startTime) {
            night = null
        }
        return night
    }

    fun doneShowingSnackBar(){
        _showSnackBarEvent.value = false
    }

    fun onStartTracking() {
//continue and update the UI.
        viewModelScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    fun onStopTracking() {
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTime = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
            tonight.value = null
            _showSnackBarEvent.value = true
        }
    }

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    private suspend fun insert(night: SleepNight) {
        database.insert(night)
    }

    private suspend fun update(night: SleepNight) {
        database.update(night)
    }

    private suspend fun clear() {
        database.clear()
    }

    val startButtonVisible = Transformations.map(tonight) {
        it == null
    }
    val stopButtonVisible = Transformations.map(tonight) {
        it != null
    }
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

}