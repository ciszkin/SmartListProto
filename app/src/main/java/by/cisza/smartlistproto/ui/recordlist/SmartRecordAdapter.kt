package by.cisza.smartlistproto.ui.recordlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import by.cisza.smartlistproto.databinding.ItemRecordBinding
import by.cisza.smartlistproto.databinding.ItemSumBinding
import by.cisza.smartlistproto.domain.SmartRecord
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SmartRecordAdapter(
    source: List<SmartRecord>,
    private val recordController: RecordController
) : RecyclerView.Adapter<SmartRecordAdapter.ViewHolder>() {

    interface RecordController {
        fun setIsDone(record: SmartRecord)
    }

    companion object {
        const val TYPE_RECORD = 0
        const val TYPE_SUM = 1

        private var list: MutableList<SmartRecord> = mutableListOf()
    }

    init {
        if (source.isNotEmpty()) {
            list.clear()
            list.addAll(
                source.filter {
                    !it.isDone
                }
            )
            val sum = list.sumOf {
                it.sum
            }
            list.add(
                SmartRecord(
                    title = "",
                    date = 0L,
                    description = null,
                    quantity = 1.0,
                    price = sum,
                    sum = sum,
                    isDone = false,
                    currency = "BYN"
                )
            )
        }
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Any)
    }

    inner class RecordViewHolder(private val binding: ItemRecordBinding) : ViewHolder(binding.root) {
        override fun bind(item: Any) {
            binding.apply {
                this.item = item as SmartRecord
                onClick = View.OnClickListener { view ->
                    view.showDescriptionDialog(item as SmartRecord)
                }
                this.onDoneClick =
                    View.OnClickListener { view ->
                    recordController.setIsDone(item as SmartRecord)
                }
                executePendingBindings()
            }
        }
    }

    private fun View.showDescriptionDialog(record: SmartRecord) {
        if (!record.description.isNullOrEmpty()) {
            MaterialAlertDialogBuilder(this.context)
                .setTitle(record.title)
                .setMessage(record.description)
                .setNeutralButton("OK", null)
                .show()
        }
    }

    inner class SumViewHolder(private val binding: ItemSumBinding) : ViewHolder(binding.root) {
        override fun bind(item: Any) {
            binding.apply {
                sum = (item as SmartRecord).sum.toString()
                currency = "BYN"
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
            list.lastIndex -> TYPE_SUM
            else -> TYPE_RECORD
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

}