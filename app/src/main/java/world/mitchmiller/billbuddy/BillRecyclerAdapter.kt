package world.mitchmiller.billbuddy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BillRecyclerAdapter internal constructor(context: Context) : RecyclerView.Adapter<BillRecyclerAdapter.BillViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var bills = emptyList<Bill>() // Cached copy of words

    companion object {
        const val NAME_SORT = 11
        const val DATE_SORT = 12
        const val NOTE_SORT = 13
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val itemView = inflater.inflate(R.layout.bill_list_item, parent, false)
        return BillViewHolder(itemView)
    }

    override fun getItemCount() = bills.size

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val current = bills[position]
        holder.billItemView.text = current.name.capitalize()
        holder.billAmount.text = String.format("$%s", current.monthlyAmount)
        holder.billDate.text = current.dueDate
        holder.billNote.text = current.notes
    }

    internal fun setBills(bills: List<Bill>) {
        this.bills = bills
        notifyDataSetChanged()
    }

    internal fun sortBills(sortMethod: Int) {
        val result = bills.sortedBy {
            when (sortMethod) {
                NAME_SORT -> {
                    it.name
                }
                DATE_SORT -> {
                    it.dueDate
                }
                NOTE_SORT -> {
                    it.notes
                }
                else -> {
                    it.name
                }
            }
        }
        setBills(result)
    }

    inner class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val billItemView: TextView = itemView.findViewById(R.id.name)
        val billAmount: TextView = itemView.findViewById(R.id.amount)
        val billDate: TextView = itemView.findViewById(R.id.due_date)
        val billNote: TextView = itemView.findViewById(R.id.note)
    }
}