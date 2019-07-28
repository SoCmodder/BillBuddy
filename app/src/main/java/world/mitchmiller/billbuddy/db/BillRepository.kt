package world.mitchmiller.billbuddy.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import world.mitchmiller.billbuddy.Bill

class BillRepository(private val billDao: BillDao) {

    val allBills: LiveData<List<Bill>> = billDao.getAllBills()

    @WorkerThread
    suspend fun insert(bill: Bill) {
        billDao.insert(bill)
    }

}