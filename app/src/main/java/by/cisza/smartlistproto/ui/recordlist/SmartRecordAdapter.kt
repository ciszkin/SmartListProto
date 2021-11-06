package by.cisza.smartlistproto.ui.recordlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import by.cisza.smartlistproto.databinding.ItemRecordBinding
import by.cisza.smartlistproto.databinding.ItemSumBinding
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.ui.fulfilment.FulfilmentDialogFragment
import by.cisza.smartlistproto.utils.round
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SmartRecordAdapter(
    private val fragment: Fragment,
    source: List<SmartRecord>,
    private val recordController: RecordController,
) : RecyclerView.Adapter<SmartRecordAdapter.ViewHolder>() {

    interface RecordController {
        fun restoreRecord(record: SmartRecord)
    }

    companion object {
        const val TYPE_RECORD = 0
        const val TYPE_SUM = 1

        private var list: MutableList<SmartRecord> = mutableListOf()

        private var sumPosition = 0
    }

    init {
        if (source.isNotEmpty()) {
            list.clear()
            list.addAll(
                source.filter {
                    it.quantityLeft > 0.0
                }
            )
//            list.sortByDescending {
//                it.quantityLeft > 0
//            }
            val sum = list.sumOf {
                ((it.quantity - it.completedQuantity) * it.price).round(2)
            }
            list.add(
                SmartRecord(
                    id = 0L,
                    title = "",
                    description = null,
                    quantity = 1.0,
                    price = sum,
                    currency = "BYN"
                )
            )
            sumPosition = list.lastIndex
            list.addAll(
                source.filter {
                    it.quantityLeft <= 0.0
                }
            )
        }
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Any)
    }

    inner class RecordViewHolder(private val binding: ItemRecordBinding) :
        ViewHolder(binding.root) {
        override fun bind(item: Any) {
            binding.apply {
                this.item = item as SmartRecord
                onItemClick = View.OnClickListener { view ->
                    view.showDescriptionDialog(item)
                }
                onDoneClick =
                    View.OnClickListener { view ->
                        if (item.quantityLeft > 0.0) {
                            fragment.showFulfilDialog(item)
                        } else {
                            isDone.isChecked = false
                            recordController.restoreRecord(item)
                        }
                    }
                executePendingBindings()
            }
        }
    }

    private fun View.showDescriptionDialog(record: SmartRecord) {
        MaterialAlertDialogBuilder(this.context)
            .setTitle(record.title)
            .setMessage(record.description)
            .setNeutralButton("OK", null)
            .show()
    }

    private fun Fragment.showFulfilDialog(record: SmartRecord) {
        FulfilmentDialogFragment(this, record).show(childFragmentManager, "fulfilmentDialog")
    }

    inner class SumViewHolder(private val binding: ItemSumBinding) : ViewHolder(binding.root) {
        override fun bind(item: Any) {
            binding.apply {
                sum = (item as SmartRecord).sum.toString()
                currency = item.currency
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val recordBinding = ItemRecordBinding.inflate(inflater, parent, false)
        val sumBinding = ItemSumBinding.inflate(inflater, parent, false)
        return when (viewType) {
            TYPE_RECORD -> RecordViewHolder(recordBinding)
            TYPE_SUM -> SumViewHolder(sumBinding)
            else -> RecordViewHolder(recordBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            sumPosition -> TYPE_SUM
            else -> TYPE_RECORD
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

}