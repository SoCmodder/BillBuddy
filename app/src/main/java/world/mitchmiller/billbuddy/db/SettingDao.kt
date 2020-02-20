package world.mitchmiller.billbuddy.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SettingDao {
    @Query("SELECT * from setting_table ORDER BY name ASC")
    fun getAllSettings(): LiveData<List<Setting>>

    @Insert
    suspend fun insertSetting(setting: Setting)

    @Query("DELETE FROM setting_table")
    fun deleteAllSettings()

    @Delete
    fun deleteSetting(setting: Setting)

    @Update
    suspend fun updateSetting(setting: Setting)
}