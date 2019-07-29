package world.mitchmiller.billbuddy

import android.app.Activity
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

    companion object {
        const val newBillActivityRequestCode = 1
        const val settingsRequestCode = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = BillRecyclerAdapter(this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        billViewModel = ViewModelProviders.of(this).get(BillViewModel::class.java)

        billViewModel.allBills.observe(this, Observer { bills ->
            // Update the cached copy of the bills in the adapter
            bills?.let { adapter.setBills(it) }
        })

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            launchNewBillActivityForResult()
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
                startActivityForResult(i, settingsRequestCode)
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchNewBillActivityForResult() {
        val intent = Intent(this, NewBillActivity::class.java)
        startActivityForResult(intent, newBillActivityRequestCode)
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
