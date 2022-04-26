package by.cisza.smartlistproto.ui.receiptitemlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

    private val args: StatisticsFragmentArgs by navArgs()

    private var _binding: FragmentStatisticsBinding? = null

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            toolbar.title = getString(R.string.fragment_statistics_title, args.recordId.toString())
            toolbar.setNavigationOnClickListener { _ ->
                findNavController().navigateUp()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}