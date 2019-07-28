package world.mitchmiller.billbuddy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class NewBillActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var saveButton: Button

    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_AMOUNT = "amount"
        const val EXTRA_NOTES = "note"
        const val EXTRA_DUE_DATE = "due_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_bill)

        nameEditText = findViewById(R.id.name_et)
        amountEditText = findViewById(R.id.amount_et)
        noteEditText = findViewById(R.id.note_et)
        saveButton = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            saveBill(
                Bill(
                    nameEditText.text.toString(),
                    amountEditText.text.toString(),
                    noteEditText.text.toString(),
                    getDateString()
                )
            )
        }
    }

    private fun getDateString(): String {
        val currentTime: Date = Calendar.getInstance(Locale.getDefault()).time
        val simpleDateFormat = SimpleDateFormat("EEE, MMM dd, YYYY", Locale.getDefault())

        return simpleDateFormat.format(currentTime)
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
            setResult(Activity.RESULT_OK, replyIntent)
        }
        finish()
    }
}