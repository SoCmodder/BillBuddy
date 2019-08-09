package world.mitchmiller.billbuddy.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import world.mitchmiller.billbuddy.BillDateUtils
import world.mitchmiller.billbuddy.R
import world.mitchmiller.billbuddy.db.Bill
import java.util.*

class EditBillActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dueDate: AppCompatImageButton
    private lateinit var dueDateValue: TextView

    var cal = Calendar.getInstance()

    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_AMOUNT = "amount"
        const val EXTRA_NOTES = "note"
        const val EXTRA_DUE_DATE = "due_date"
        const val EXTRA_PAID = "paid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_bill)

        nameEditText = findViewById(R.id.name_et)
        amountEditText = findViewById(R.id.amount_et)
        noteEditText = findViewById(R.id.note_et)
        saveButton = findViewById(R.id.save_button)
        dueDate = findViewById(R.id.due_date_button)
        dueDateValue = findViewById(R.id.due_date_value)

        saveButton.setOnClickListener {
            saveBill(
                Bill(
                    nameEditText.text.toString(),
                    amountEditText.text.toString(),
                    noteEditText.text.toString(),
                    dueDateValue.text.toString(),
                    false
                )
            )
        }

        dueDate.setOnClickListener {

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

            DatePickerDialog(this@EditBillActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        updateDateInView()
    }

    private fun updateDateInView() {
        dueDateValue.text = BillDateUtils.getBillDateStringFromDate(cal.time)
    }

    private fun fillCurrentDetails(bill: Bill) {
        nameEditText.setText(bill.name)
        amountEditText.setText(bill.monthlyAmount)
        noteEditText.setText(bill.notes)
        dueDateValue.setText(bill.dueDate)
    }

    private fun saveBill(bill: Bill) {
        val replyIntent = Intent()
        if (TextUtils.isEmpty(nameEditText.text)) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
        } else {
            replyIntent.putExtra(EXTRA_NAME, bill.name)
            replyIntent.putExtra(EXTRA_AMOUNT, bill.monthlyAmount)
            replyIntent.putExtra(EXTRA_NOTES, bill.notes)
            replyIntent.putExtra(EXTRA_DUE_DATE, bill.dueDate)
            replyIntent.putExtra(EXTRA_PAID, bill.paid)
            setResult(Activity.RESULT_OK, replyIntent)
        }
        finish()
    }
}