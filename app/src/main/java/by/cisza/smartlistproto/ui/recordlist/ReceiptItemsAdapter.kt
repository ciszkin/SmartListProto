package by.cisza.smartlistproto.ui.recordlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.cisza.smartlistproto.databinding.ItemReceiptRecordBinding
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.utils.toAmount

class ReceiptItemsAdapter(
    source: List<ReceiptItem>
) : RecyclerView.Adapter<ReceiptItemsAdapter.ViewHolder>() {

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
                recordName.text = (item as ReceiptItem).title
                val recordQuantityAndPriceString = "${item.quantity} x ${item.price} ${item.currency}"
                recordQuantityAndPrice.text = recordQuantityAndPriceString
                recordSum.text = item.sum.toAmount(item.currency)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val recordBinding = ItemReceiptRecordBinding.inflate(inflater, parent, false)
        return when (viewType) {
            TYPE_RECORD -> RecordViewHolder(recordBinding)
            else -> RecordViewHolder(recordBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_RECORD
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

}