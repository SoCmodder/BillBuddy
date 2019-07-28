package world.mitchmiller.billbuddy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bill_table")
data class Bill(
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    val monthlyAmount: String,
    val notes: String,
    val dueDate: String,
    val paid: Boolean
)