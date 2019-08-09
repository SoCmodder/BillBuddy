package world.mitchmiller.billbuddy.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class BillRepository(private val billDao: BillDao) {
    val allBills: LiveData<List<Bill>> = billDao.getAllBills()

    @WorkerThread
    suspend fun insert(bill: Bill) {
        billDao.insert(bill)
    }

    @WorkerThread
    fun delete(bill: Bill) {
        billDao.delete(bill)
    }

    @WorkerThread
    fun deleteAll() {
        billDao.deleteAll()
    }

    @WorkerThread
    suspend fun updateBill(bill: Bill) {
        billDao.update(bill)
    }
}