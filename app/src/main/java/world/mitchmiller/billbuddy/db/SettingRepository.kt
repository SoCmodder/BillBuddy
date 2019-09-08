package world.mitchmiller.billbuddy.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class SettingRepository(private val settingDao: SettingDao) {
    val allSettings: LiveData<List<Setting>> = settingDao.getAllSettings()

    @WorkerThread
    suspend fun insert(setting: Setting) {
        settingDao.insert(setting)
    }

    @WorkerThread
    fun delete(setting: Setting) {
        settingDao.delete(setting)
    }

    @WorkerThread
    fun deleteAll() {
        settingDao.deleteAll()
    }

    @WorkerThread
    suspend fun updateSetting(setting: Setting) {
        settingDao.update(setting)
    }
}