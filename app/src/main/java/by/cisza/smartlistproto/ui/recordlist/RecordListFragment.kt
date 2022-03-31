package by.cisza.smartlistproto.ui.recordlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.cisza.smartlistproto.databinding.FragmentRecordListBinding
import by.cisza.smartlistproto.domain.Receipt.ReceiptItem
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.ui.fulfilment.FulfilmentDialogFragment
import by.cisza.smartlistproto.ui.record.RecordDialogFragment
import by.cisza.smartlistproto.utils.round
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class RecordListFragment : Fragment(), RecordDialogFragment.RecordDialogListener,
    FulfilmentDialogFragment.FulfilmentDialogListener {

    @ExperimentalCoroutinesApi
    private lateinit var viewModel: RecordListViewModel

    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>
    // This property is only valid between onCreateView and
    // onDestroyView.


    private var _binding: FragmentRecordListBinding? = null
    private val binding get() = _binding

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.records.collect { newList ->
                    updateList(newList)
                    Log.e("MyDebug", "New list: $newList")
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.receiptItems.collect { newItems ->
                    updateReceipt(newItems)
                    Log.e("MyDebug", "New items: $newItems")
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(RecordListViewModel::class.java)
        _binding = FragmentRecordListBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        updateList()
        with(binding) {
            bottomSheet = BottomSheetBehavior.from(this!!.bottomSheetReceipt)
            this.onFabClick = View.OnClickListener {
                RecordDialogFragment(this@RecordListFragment).show(
                    childFragmentManager,
                    "NewRecordDialog"
                )
            }
        }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @ExperimentalCoroutinesApi
    override fun onDialogResult(record: SmartRecord) {
        viewModel.addRecord(record)
        updateList(viewModel.records.value)
//        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED

    }

    @ExperimentalCoroutinesApi
    override fun onDialogResult(receiptItem: ReceiptItem) {
        viewModel.fulfilRecord(receiptItem)
        if (receiptItem.quantity == 0.0) updateList(viewModel.records.value)
    }

    @ExperimentalCoroutinesApi
    private fun updateList(newList: List<SmartRecord>) {
        if (binding != null) {
            with(binding) {
                this!!.list.adapter = SmartRecordAdapter(
                    fragment = this@RecordListFragment,
                    source = newList,
                    recordController = viewModel
                )
                list.layoutManager = LinearLayoutManager(context)
                list.hasFixedSize()
            }
        }
    }

    private fun updateReceipt(newItems: List<ReceiptItem>) {
        if (binding != null) {
            with(binding) {
                val sum = newItems.sumOf {
                    it.sum
                }

                this!!.receiptVisible = sum > 0.0
                if (sum > 0.0) {
                    totalSum = sum.round(2).toString()
                    receiptList.adapter = ReceiptItemsAdapter(
                        source = newItems,
//                        recordController = viewModel
                    )
                    receiptList.layoutManager = LinearLayoutManager(context)
                    receiptList.hasFixedSize()
                }
            }
        }
    }
}