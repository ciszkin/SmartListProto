package by.cisza.smartlistproto.ui.recordlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.cisza.smartlistproto.databinding.ItemReceiptRecordBinding
import by.cisza.smartlistproto.model.ReceiptItem

class ReceiptItemsAdapter(
    source: List<ReceiptItem>,
//    private val recordController: SmartRecordAdapter.RecordController
) : RecyclerView.Adapter<ReceiptItemsAdapter.ViewHolder>() {

//    interface RecordController {
//        fun setIsDone(record: SmartRecord)
//    }

    companion object {
        const val TYPE_RECORD = 0
        const val TYPE_SUM = 1

        private var list: MutableList<ReceiptItem> = mutableListOf()
    }

    init {
        if (source.isNotEmpty()) {
            list.clear()
            list.addAll(
                source
            )
        }
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Any)
    }

    inner class RecordViewHolder(private val binding: ItemReceiptRecordBinding) :
        ViewHolder(binding.root) {
        override fun bind(item: Any) {
            binding.apply {
                this.item = item as ReceiptItem
//                onItemClick = View.OnClickListener { view ->
//                    view.showDescriptionDialog(item)
//                }
//                onDoneClick = View.OnClickListener { view ->
//                    recordController.setIsDone(item)
//                }
                executePendingBindings()
            }
        }
    }

//    private fun View.showDescriptionDialog(record: SmartRecord) {
//        MaterialAlertDialogBuilder(this.context)
//            .setTitle(record.title)
//            .setMessage(record.description)
//            .setNeutralButton("OK", null)
//            .show()
//    }

//    inner class SumViewHolder(private val binding: ItemSumBinding) : ViewHolder(binding.root) {
//        override fun bind(item: Any) {
//            binding.apply {
//                sum = (item as SmartRecord).sum.toString()
//                currency = item.currency
//            }
//        }
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val recordBinding = ItemReceiptRecordBinding.inflate(inflater, parent, false)
//        val sumBinding = ItemSumBinding.inflate(inflater, parent, false)
        return when (viewType) {
            TYPE_RECORD -> RecordViewHolder(recordBinding)
//            TYPE_SUM -> SumViewHolder(sumBinding)
            else -> RecordViewHolder(recordBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_RECORD
//        return when (position) {
//            list.lastIndex -> TYPE_SUM
//            else -> TYPE_RECORD
//        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

}