package world.mitchmiller.billbuddy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import world.mitchmiller.billbuddy.db.BillRepository
import world.mitchmiller.billbuddy.db.BillRoomDatabase

class BillViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BillRepository
    val allBills: LiveData<List<Bill>>

    init {
        val billsDao = BillRoomDatabase.getDatabase(application, viewModelScope).billDao()
        repository = BillRepository(billsDao)
        allBills = repository.allBills
    }

    fun insert(bill: Bill) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(bill)
    }

    fun delete(bill: Bill) {
        repository.delete(bill)
    }

    fun deleteAll() {
        repository.deleteAll()
    }
}