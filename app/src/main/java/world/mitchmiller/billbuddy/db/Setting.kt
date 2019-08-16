package world.mitchmiller.billbuddy.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setting_table")
data class Setting(
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    val value: String,
    val boolValue: Boolean
)