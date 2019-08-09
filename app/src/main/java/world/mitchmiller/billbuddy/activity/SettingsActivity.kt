package world.mitchmiller.billbuddy.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import world.mitchmiller.billbuddy.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var incomeET: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        incomeET = findViewById(R.id.monthly_income_et)
        saveButton = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            saveSettings()
        }
    }

    private fun saveSettings() {

    }
}