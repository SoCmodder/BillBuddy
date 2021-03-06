package world.mitchmiller.billbuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import world.mitchmiller.billbuddy.db.*

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SettingRepository
    val allSettings: LiveData<List<Setting>>

    init {
        val settingsDao = BillRoomDatabase.getDatabase(application, viewModelScope).settingDao()
        repository = SettingRepository(settingsDao)
        allSettings = repository.allSettings
    }

    fun insert(setting: Setting) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(setting)
    }

    fun delete(setting: Setting) {
        repository.delete(setting)
    }

    fun deleteAll() {
        repository.deleteAll()
    }

    fun updateSetting(setting: Setting) = viewModelScope.launch(Dispatchers.IO){
        repository.updateSetting(setting)
    }
}