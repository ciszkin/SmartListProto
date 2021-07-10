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
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.ui.newrecord.NewRecordDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class RecordListFragment : Fragment(), NewRecordDialogFragment.NewRecordDialogListener {

    private var _binding: FragmentRecordListBinding? = null
    @ExperimentalCoroutinesApi
    private lateinit var viewModel: RecordListViewModel
    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.records.collect { newList ->
                    updateList(newList)
                    Log.e("MyDebug", "New list: ${newList}")
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

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.records.collect { newList ->
//                    updateList(viewModel.records.value)
//                    Log.e("MyDebug", "New list: ${viewModel.records.value}")
//                }
//            }
//        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        updateList()
        with(binding) {
            bottomSheet = BottomSheetBehavior.from(this!!.bottomSheetTotal)
            this.onFabClick = View.OnClickListener {
                NewRecordDialogFragment(this@RecordListFragment).show(
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
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED

    }

    private fun updateList(newList: List<SmartRecord>) {
        if (binding != null) {
            with(binding) {
    //            val newList = viewModel.getRecordsList()
                this!!.list.adapter = SmartRecordAdapter(
                    source = newList,
                    recordController = viewModel
                )
                list.layoutManager = LinearLayoutManager(context)
                list.hasFixedSize()
                totalSum = newList.sumOf { record ->
                    record.sum
                }.toString()
            }
        }
    }
}