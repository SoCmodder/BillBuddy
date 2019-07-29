package world.mitchmiller.billbuddy

import java.text.SimpleDateFormat
import java.util.*

object BillDateUtils {
    fun getBillDateFromString(billDate: String): Date {
        val simpleDateFormat = SimpleDateFormat("EEE, MMM dd, YYYY", Locale.getDefault())
        return simpleDateFormat.parse(billDate)
    }

    fun getBillDateStringFromDate(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("EEE, MMM dd, YYYY", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}