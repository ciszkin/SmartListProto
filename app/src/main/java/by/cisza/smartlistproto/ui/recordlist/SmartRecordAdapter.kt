package by.cisza.smartlistproto.ui.recordlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.cisza.smartlistproto.databinding.ItemRecordBinding
import by.cisza.smartlistproto.databinding.ItemSumBinding
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.utils.round
import by.cisza.smartlistproto.utils.toAmount
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SmartRecordAdapter(
    source: List<SmartRecord>,
    private val recordController: RecordController,
) : RecyclerView.Adapter<SmartRecordAdapter.ViewHolder>() {

    interface RecordController {
        fun restoreRecord(record: SmartRecord)
        fun fulfilRecord(item: SmartRecord)
        fun addRecord()
        fun showStatistics(record: SmartRecord)
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
            if (item is SmartRecord) binding.apply {
                itemCard.setOnClickListener{ view ->
                    view.showDescriptionDialog(item)
                }

                isDone.isChecked = item.quantityLeft == 0.0
                isDone.setOnClickListener { view ->
                    if (item.quantityLeft > 0.0) {
                        recordController.fulfilRecord(item)
                    } else {
                        isDone.isChecked = false
                        recordController.restoreRecord(item)
                    }
                }

                recordName.text = item.title

                val visibility = if (item.quantityLeft == 0.0) View.INVISIBLE else View.VISIBLE
                val quantityAndPrice = "${item.quantityLeft} x ${item.price.toAmount(item.currency)}"
                recordQuantityAndPrice.text = quantityAndPrice
                recordQuantityAndPrice.visibility = visibility

                recordDescription.text = item.description
                recordDescription.visibility = visibility

                recordSum.text = item.sum.toAmount(item.currency)
                recordSum.visibility = visibility

                progressIndicator.progress = (item.completedQuantity / item.quantity * 100).toInt()
                progressIndicator.visibility = visibility
            }
        }
    }

    private fun View.showDescriptionDialog(record: SmartRecord) {
        MaterialAlertDialogBuilder(this.context)
            .setTitle(record.title)
            .setMessage(record.description)
            .setNeutralButton("Show Statistics") { dialog, _ ->
                recordController.showStatistics(record)
                dialog.dismiss()
            }
            .setPositiveButton("Ok",null)
            .show()
    }

    inner class SumViewHolder(private val binding: ItemSumBinding) : ViewHolder(binding.root) {
        override fun bind(item: Any) {
            binding.apply {
                itemSumValue.text = (item as SmartRecord).sum.toAmount(item.currency)
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