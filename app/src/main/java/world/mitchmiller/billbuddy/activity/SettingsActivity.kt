package world.mitchmiller.billbuddy.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import world.mitchmiller.billbuddy.R
import world.mitchmiller.billbuddy.db.Setting
import world.mitchmiller.billbuddy.viewmodel.BillViewModel
import world.mitchmiller.billbuddy.viewmodel.SettingViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingViewModel: SettingViewModel
    private lateinit var incomeET: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        settingViewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)

        incomeET = findViewById(R.id.monthly_income_et)
        saveButton = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            val income = incomeET.text.toString()
            updateSetting(Setting("income", income, false))
        }

        settingViewModel.allSettings.observe(this, Observer { settings ->

            settings?.let {
                //update the view
            }
        })
    }

    private fun updateSetting(setting: Setting) {
        settingViewModel.updateSetting(setting)
    }
}