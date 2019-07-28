package world.mitchmiller.billbuddy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import world.mitchmiller.billbuddy.Bill

@Database(entities = [Bill::class], version = 2)
abstract class BillRoomDatabase : RoomDatabase() {

    abstract fun billDao(): BillDao

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
                    populateDatabase(database.billDao())
                }
            }
        }

        suspend fun populateDatabase(billDao: BillDao) {
            billDao.deleteAll()

            var bill = Bill("Rent", "400.00", "I miss St. Louis rent prices", "August 20 2019")
            billDao.insert(bill)
            bill = Bill("Electricity", "100.00", "zap", "August 20 2019")
            billDao.insert(bill)
        }
    }
}