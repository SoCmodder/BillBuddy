package world.mitchmiller.billbuddy

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

fun Activity.showAreYouSureDialog(dcl: DialogInterface.OnClickListener) {
    lateinit var dialog: AlertDialog

    val builder = AlertDialog.Builder(this)
    builder.setTitle("are you sure?")
    builder.setMessage("are you sure you want to save changes?")
    builder.setPositiveButton("YES", dcl)
    builder.setNegativeButton("NO", dcl)

    dialog = builder.create()
    dialog.show()
}