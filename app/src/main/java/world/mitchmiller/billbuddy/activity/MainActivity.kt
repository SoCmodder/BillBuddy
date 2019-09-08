package world.mitchmiller.billbuddy.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import world.mitchmiller.billbuddy.*
import world.mitchmiller.billbuddy.BillRecyclerAdapter.Companion.DATE_SORT
import world.mitchmiller.billbuddy.BillRecyclerAdapter.Companion.NAME_SORT
import world.mitchmiller.billbuddy.BillRecyclerAdapter.Companion.NOTE_SORT
import world.mitchmiller.billbuddy.db.Bill
import world.mitchmiller.billbuddy.viewmodel.BillViewModel

//TODO: TO ADD:
/**
 * - Link to app associated with bill
 * - Different ways to sort
 * - Delete bills individually
 * - Add menu and button for deleting all
 * - Connect settings activity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var billViewModel: BillViewModel
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: BillRecyclerAdapter

    companion object {
        const val newBillActivityRequestCode = 1
        const val settingsRequestCode = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        billViewModel = ViewModelProviders.of(this).get(BillViewModel::class.java)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = BillRecyclerAdapter(
            this,
            BillClickListener(billViewModel)
        )
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        billViewModel.allBills.observe(this, Observer { bills ->
            // Update the cached copy of the bills in the adapter
            bills?.let { adapter.setBills(it) }
        })

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            launchNewBillActivityForResult()
        }
    }

    class BillClickListener(billViewModel: BillViewModel) :
        BillRecyclerAdapter.OnItemClickListener {
        val vm = billViewModel
        override fun onItemClick(bill: Bill) {
            val b = bill.copy(paid = !bill.paid)
            vm.updateBill(b)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivityForResult(i,
                    settingsRequestCode
                )
            }
            R.id.delete_all -> {
                val dialogClickListener = DialogInterface.OnClickListener{_,which ->
                    when(which){
                        DialogInterface.BUTTON_POSITIVE -> billViewModel.deleteAll()
                        DialogInterface.BUTTON_NEGATIVE -> Log.d("MAIN", "Canceled")
                    }
                }
                showAreYouSureDialog(dialogClickListener)
            }
            R.id.sort -> {
                showSortDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val sortListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> adapter.sortBills(NAME_SORT)
                DialogInterface.BUTTON_NEGATIVE -> adapter.sortBills(DATE_SORT)
                DialogInterface.BUTTON_NEUTRAL -> adapter.sortBills(NOTE_SORT)
            }
        }

        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sort")
        builder.setMessage("which sort method?")
        builder.setPositiveButton("name", sortListener)
        builder.setNeutralButton("date", sortListener)
        builder.setNegativeButton("note", sortListener)

        dialog = builder.create()
        dialog.show()
    }



    private fun launchNewBillActivityForResult() {
        val intent = Intent(this, NewBillActivity::class.java)
        startActivityForResult(intent,
            newBillActivityRequestCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newBillActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
                val bill = Bill(
                    it.getStringExtra(NewBillActivity.EXTRA_NAME),
                    it.getStringExtra(NewBillActivity.EXTRA_AMOUNT),
                    it.getStringExtra(NewBillActivity.EXTRA_NOTES),
                    it.getStringExtra(NewBillActivity.EXTRA_DUE_DATE),
                    it.getBooleanExtra(NewBillActivity.EXTRA_PAID, false)
                )
                billViewModel.insert(bill)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
