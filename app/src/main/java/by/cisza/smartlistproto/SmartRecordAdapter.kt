package by.cisza.smartlistproto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.cisza.smartlistproto.databinding.ItemRecordBinding
import by.cisza.smartlistproto.domain.SmartRecord
import com.google.android.material.snackbar.Snackbar

class SmartRecordAdapter(private val list: List<SmartRecord>) : RecyclerView.Adapter<SmartRecordAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SmartRecord) {
            binding.apply {
                this.item = item
                onClick = View.OnClickListener { view ->
                    Snackbar.make(view, "Amount: ${item.sum}", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecordBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

}