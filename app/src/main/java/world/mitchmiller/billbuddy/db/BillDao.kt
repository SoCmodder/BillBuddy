package world.mitchmiller.billbuddy.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BillDao {

    @Query("SELECT * from bill_table ORDER BY name ASC")
    fun getAllBills(): LiveData<List<Bill>>

    @Insert
    suspend fun insert(bill: Bill)

    @Query("DELETE FROM bill_table")
    fun deleteAll()

    @Delete
    fun delete(bill: Bill)

    @Update
    suspend fun update(bill: Bill)
}