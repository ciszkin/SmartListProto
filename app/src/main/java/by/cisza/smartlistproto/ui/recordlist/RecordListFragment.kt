package by.cisza.smartlistproto.ui.recordlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.cisza.smartlistproto.databinding.FragmentRecordListBinding
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.ui.fulfilment.FulfilmentDialogFragment
import by.cisza.smartlistproto.ui.record.RecordDialogFragment
import by.cisza.smartlistproto.utils.updateReceipt
import by.cisza.smartlistproto.utils.updateSmartList
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val TAG = "MyDebug"

class RecordListFragment : Fragment(), RecordDialogFragment.RecordDialogListener,
    FulfilmentDialogFragment.FulfilmentDialogListener, View.OnClickListener {

    private lateinit var viewModel: RecordListViewModel

    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>


    private var _binding: FragmentRecordListBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    updateView(state)
                }
            }
        }
    }


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

//        updateView(viewModel.viewState.value)

        binding?.apply {
            bottomSheet = BottomSheetBehavior.from(bottomSheetReceipt)
        }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDialogResult(record: SmartRecord?) {
        viewModel.addRecord(record)
//        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDialogResult(receiptItem: ReceiptItem) {
        viewModel.fulfilRecord(receiptItem)
    }

    private fun updateView(state: RecordListViewState) {
        requireNotNull(binding)
        binding?.apply {

            floatingActionButton.setOnClickListener(this@RecordListFragment::onClick)
            list.updateSmartList(
                recordController = viewModel,
                source = state.records)
            receiptList.updateReceipt(
                source = state.receiptItems
            )
            bottomSheetReceipt.visibility = if (state.isReceiptVisible) View.VISIBLE else View.INVISIBLE
            totalSum.text = state.totalSum.toString()

            state.itemToFulfil?.let {
                FulfilmentDialogFragment(
                    listener = this@RecordListFragment as FulfilmentDialogFragment.FulfilmentDialogListener,
                    record = it
                ).show(childFragmentManager, "fulfilmentDialog")
            }
            if (state.showNewRecordDialog)
                RecordDialogFragment(this@RecordListFragment).show(
                    childFragmentManager,
                    "NewRecordDialog"
                )
        }
    }

    override fun onClick(v: View?) {
        viewModel.addRecord()
    }

}