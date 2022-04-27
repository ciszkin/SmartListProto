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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    updateView(state)
                }
            }
        }

        binding?.apply {
            bottomSheet = BottomSheetBehavior.from(bottomSheetReceiptLayout.bottomSheetReceipt)
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
        viewModel.handleReceiptItem(receiptItem)
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "from: ${this.javaClass.simpleName} we're here!")

    }

    private fun updateView(state: RecordListViewState) {
        requireNotNull(binding)
        binding?.apply {

            floatingActionButton.setOnClickListener(this@RecordListFragment::onClick)
            bottomSheetReceiptLayout.saveReceiptButton.setOnClickListener(this@RecordListFragment::onClick)
            list.updateSmartList(
                recordController = viewModel,
                source = state.records)
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

            state.itemToFulfil?.let {
                FulfilmentDialogFragment(
                    listener = this@RecordListFragment as FulfilmentDialogFragment.FulfilmentDialogListener,
                    record = it
                ).show(childFragmentManager, "fulfilmentDialog")
            }
            state.itemToShowStatistics?.let {
                val direction = RecordListFragmentDirections.showStatistics(it.id)
                findNavController().navigate(direction)

            }
            if (state.showNewRecordDialog)
                RecordDialogFragment(this@RecordListFragment).show(
                    childFragmentManager,
                    "NewRecordDialog"
                )
            if (state.showSaveReceiptCompletion) {
                view?.let {
                    Snackbar.make(it, "Receipt saved successfully!", Snackbar.LENGTH_SHORT).show()
                }
//                viewModel.clearShowSaveReceiptCompletionFlag()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.floating_action_button -> viewModel.addRecord()
            R.id.save_receipt_button -> viewModel.saveReceipt()
        }
    }
}