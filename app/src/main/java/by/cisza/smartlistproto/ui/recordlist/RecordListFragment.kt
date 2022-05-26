package by.cisza.smartlistproto.ui.recordlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.databinding.FragmentRecordListBinding
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.ui.fulfilmentdialog.FulfilmentDialogFragment
import by.cisza.smartlistproto.ui.recorddialog.RecordDialogFragment
import by.cisza.smartlistproto.utils.updateReceipt
import by.cisza.smartlistproto.utils.updateSmartList
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val TAG = "MyDebug"

@AndroidEntryPoint
class RecordListFragment : Fragment(), RecordDialogFragment.RecordDialogListener,
    FulfilmentDialogFragment.FulfilmentDialogListener, View.OnClickListener {

    private val viewModel: RecordListViewModel by viewModels()

    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    private var _binding: FragmentRecordListBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordListBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        updateView(viewModel.viewState.value)

        binding?.apply {
            bottomSheet = BottomSheetBehavior.from(bottomSheetReceiptLayout.bottomSheetReceipt)
        }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    updateView(state)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collectLatest { event ->
                    handleEvent(event)
                }
            }
        }
    }


    private fun handleEvent(event: RecordListEvent) {
        when (event) {
            is ReceiptSavedEvent -> {
                view?.let {
                    Snackbar.make(it, event.message, Snackbar.LENGTH_SHORT).show()
                }
            }
            is ShowStatisticsEvent -> {
                val direction = RecordListFragmentDirections.showStatistics(event.record.id)
                findNavController().navigate(direction)
            }
            is FulfilEvent -> {
                FulfilmentDialogFragment(
                    listener = this@RecordListFragment as FulfilmentDialogFragment.FulfilmentDialogListener,
                    record = event.record
                ).show(childFragmentManager, "fulfilmentDialog")
            }
            is UpdateViewEvent -> {
                updateView(event.state)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDialogResult(record: SmartRecord?) {
        viewModel.addRecord(record)
//        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDialogResult(receiptItem: ReceiptItem?) {
        viewModel.handleReceiptItem(receiptItem)
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "from: ${this.javaClass.simpleName} we're here!")

    }

    private fun updateView(state: RecordListViewState) {
        requireNotNull(binding)
        binding?.run {

            floatingActionButton.setOnClickListener(this@RecordListFragment::onClick)
            bottomSheetReceiptLayout.saveReceiptButton.setOnClickListener(this@RecordListFragment::onClick)
            list.updateSmartList(
                recordController = viewModel,
                source = state.records
            )
            bottomSheetReceiptLayout.receiptList.updateReceipt(
                source = state.receiptItems
            )
            if (state.isReceiptVisible) {
                bottomSheetReceiptLayout.bottomSheetReceipt.visibility = View.VISIBLE
            } else {
                bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetReceiptLayout.bottomSheetReceipt.visibility = View.INVISIBLE
            }
            bottomSheetReceiptLayout.totalSum.text = state.totalSum.toString()

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.floating_action_button ->
                RecordDialogFragment(this@RecordListFragment).show(
                    childFragmentManager,
                    "NewRecordDialog"
                )
            R.id.save_receipt_button -> viewModel.saveReceipt()
        }
    }
}