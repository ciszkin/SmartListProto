package by.cisza.smartlistproto.ui.recordlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.cisza.smartlistproto.databinding.FragmentRecordListBinding
import by.cisza.smartlistproto.model.ReceiptItem
import by.cisza.smartlistproto.model.SmartRecord
import by.cisza.smartlistproto.ui.fulfilment.FulfilmentDialogFragment
import by.cisza.smartlistproto.ui.record.RecordDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class RecordListFragment : Fragment(), RecordDialogFragment.RecordDialogListener,
    FulfilmentDialogFragment.FulfilmentDialogListener {

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

        with(binding) {
            bottomSheet = BottomSheetBehavior.from(this!!.bottomSheetReceipt)
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

    override fun onDialogResult(record: SmartRecord?) {
        viewModel.addRecord(record)
//        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDialogResult(receiptItem: ReceiptItem) {
        viewModel.fulfilRecord(receiptItem)
    }

    private fun updateView(state: RecordListViewState) {
        if (binding != null) with(binding) {
            this!!.state = state
            recordController = viewModel
            state.itemToFulfil?.let {
                FulfilmentDialogFragment(
                    this@RecordListFragment  as FulfilmentDialogFragment.FulfilmentDialogListener,
                    it
                ).show(childFragmentManager, "fulfilmentDialog")
            }
            if (state.showNewRecordDialog)
                RecordDialogFragment(this@RecordListFragment).show(
                    childFragmentManager,
                    "NewRecordDialog"
                )
        }
    }

}