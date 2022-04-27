package by.cisza.smartlistproto.ui.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.databinding.FragmentStatisticsBinding
import by.cisza.smartlistproto.utils.updateReceipt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private val args: StatisticsFragmentArgs by navArgs()

    private val viewModel: StatisticsViewModel by viewModels()

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

        viewModel.getData(args.recordId)

        binding?.apply {
            toolbar.title = getString(R.string.fragment_statistics_title, args.recordId.toString())
            toolbar.setNavigationOnClickListener { _ ->
                findNavController().navigateUp()
            }
        }

        lifecycleScope.launchWhenResumed {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    updateView(state)
                }
            }
        }
    }

    private fun updateView(state: StatisticsViewState) {
        binding?.apply {
            loading.isVisible = state.loading
            list.updateReceipt(state.items)
            toolbar.title = getString(R.string.fragment_statistics_title, state.record?.title)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}