package world.mitchmiller.billbuddy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import world.mitchmiller.billbuddy.db.Bill

class BillRecyclerAdapter internal constructor(context: Context, itemClickListener: OnItemClickListener) : RecyclerView.Adapter<BillRecyclerAdapter.BillViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var bills = emptyList<Bill>() // Cached copy of words
    private val listener: OnItemClickListener = itemClickListener

    companion object {
        const val NAME_SORT = 11
        const val DATE_SORT = 12
        const val NOTE_SORT = 13
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val itemView = inflater.inflate(R.layout.bill_list_item, parent, false)
        return BillViewHolder(itemView)
    }

    public interface OnItemClickListener {
        fun onItemClick(bill: Bill)
    }

    override fun getItemCount() = bills.size

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val current = bills[position]
        holder.billItemView.text = current.name.capitalize()
        holder.billAmount.text = String.format("$%s", current.monthlyAmount)
        holder.billDate.text = current.dueDate
        holder.billNote.text = current.notes
        if (current.paid) {
            holder.billPaidStatus.visibility = View.VISIBLE
        } else {
            holder.billPaidStatus.visibility = View.GONE
        }

        holder.cardview.setOnClickListener {
            listener.onItemClick(current)
        }
    }

    internal fun setBills(bills: List<Bill>) {
        this.bills = bills
        notifyDataSetChanged()
    }

    internal fun sortBills(sortMethod: Int) {
        val result = bills.sortedBy {
            when (sortMethod) {
                NAME_SORT -> it.name
                DATE_SORT -> it.dueDate
                NOTE_SORT -> it.notes
                else -> it.name
            }
        }
        setBills(result)
    }

    inner class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardview: CardView = itemView.findViewById(R.id.cardview)
        val billItemView: TextView = itemView.findViewById(R.id.name)
        val billAmount: TextView = itemView.findViewById(R.id.amount)
        val billDate: TextView = itemView.findViewById(R.id.due_date)
        val billNote: TextView = itemView.findViewById(R.id.note)
        val billPaidStatus: ImageView = itemView.findViewById(R.id.paid_status)
    }
}