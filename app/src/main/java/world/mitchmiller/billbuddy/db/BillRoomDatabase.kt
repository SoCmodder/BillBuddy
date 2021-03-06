package world.mitchmiller.billbuddy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Bill::class, Setting::class], version = 5, exportSchema = false)
abstract class BillRoomDatabase : RoomDatabase() {

    abstract fun billDao(): BillDao
    abstract fun settingDao(): SettingDao

    companion object {
        @Volatile
        private var INSTANCE: BillRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BillRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BillRoomDatabase::class.java,
                    "Bill_database"
                )
                    .addCallback(BillDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    private class BillDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialSettings(database.settingDao())
                }
            }
        }

        // insert default settings
        private suspend fun populateInitialSettings(dao: SettingDao) {
            if (dao.getAllSettings().value?.size == 0) {
                dao.insertSetting(Setting("settings_version", "1", false))
                dao.insertSetting(Setting("income", "0.00", false))
            }
        }
    }
}