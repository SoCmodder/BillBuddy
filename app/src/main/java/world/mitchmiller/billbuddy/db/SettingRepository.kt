package world.mitchmiller.billbuddy.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class SettingRepository(private val settingDao: SettingDao) {
    val allSettings: LiveData<List<Setting>> = settingDao.getAllSettings()

    @WorkerThread
    suspend fun insert(setting: Setting) {
        settingDao.insertSetting(setting)
    }

    @WorkerThread
    fun delete(setting: Setting) {
        settingDao.deleteSetting(setting)
    }

    @WorkerThread
    fun deleteAll() {
        settingDao.deleteAllSettings()
    }

    @WorkerThread
    suspend fun updateSetting(setting: Setting) {
        settingDao.updateSetting(setting)
    }
}