package by.cisza.smartlistproto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.cisza.smartlistproto.databinding.FragmentRecordListBinding
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.ui.newrecord.NewRecordDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.util.*

class RecordListFragment : Fragment(), NewRecordDialogFragment.NewRecordDialogListener {

    private var _binding: FragmentRecordListBinding? = null
    private lateinit var viewModel: RecordListViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RecordListViewModel::class.java)
        _binding = FragmentRecordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateList()
        with(binding) {
            onFabClick = View.OnClickListener {
                Log.e("My","It's working!")
            }
            onAddClick = View.OnClickListener {
                NewRecordDialogFragment(this@RecordListFragment).show(childFragmentManager, "NewRecordDialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDialogResult(record: SmartRecord) {
        viewModel.addRecord(record)
        updateList()

    }

    private fun updateList() {
        with(binding) {
            list.adapter = SmartRecordAdapter(viewModel.getRecordsList())
            list.layoutManager = LinearLayoutManager(context)
            list.hasFixedSize()
        }
    }
}