package by.cisza.smartlistproto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.cisza.smartlistproto.databinding.FragmentRecordListBinding
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.ui.newrecord.NewRecordDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class RecordListFragment : Fragment(), NewRecordDialogFragment.NewRecordDialogListener {

    private var _binding: FragmentRecordListBinding? = null
    private lateinit var viewModel: RecordListViewModel
    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(RecordListViewModel::class.java)
        _binding = FragmentRecordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateList()
        with(binding) {
            bottomSheet = BottomSheetBehavior.from(this.bottomSheetTotal)
            onFabClick = View.OnClickListener {
                NewRecordDialogFragment(this@RecordListFragment).show(childFragmentManager, "NewRecordDialog")
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

    override fun onDialogResult(record: SmartRecord) {
        viewModel.addRecord(record)
        updateList()
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED

    }

    private fun updateList() {
        with(binding) {
            val newList = viewModel.getRecordsList()
            list.adapter = SmartRecordAdapter(newList)
            list.layoutManager = LinearLayoutManager(context)
            list.hasFixedSize()

            totalSum = newList.sumOf { record ->
                record.sum
            }.toString()
        }
    }
}