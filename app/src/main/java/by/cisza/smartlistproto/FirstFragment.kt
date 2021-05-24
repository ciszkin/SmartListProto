package by.cisza.smartlistproto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.cisza.smartlistproto.databinding.FragmentFirstBinding
import by.cisza.smartlistproto.domain.SmartRecord
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.adapter = SmartRecordAdapter(listOf(
            SmartRecord("Бананы", Calendar.getInstance().timeInMillis,null, 2.19, "р"),
            SmartRecord("Хлеб", Calendar.getInstance().timeInMillis,null, 0.79, "р"),
            SmartRecord("Рис", Calendar.getInstance().timeInMillis,null, 1.2, "р"),
            SmartRecord("Молоко", Calendar.getInstance().timeInMillis,null, 1.1, "р"),
            SmartRecord("Оливки", Calendar.getInstance().timeInMillis,null, 1.79, "р"),
            SmartRecord("Мука", Calendar.getInstance().timeInMillis,null, 2.44, "р"),
            SmartRecord("Пельмени", Calendar.getInstance().timeInMillis,null, 3.17, "р"),
            SmartRecord("Старка", Calendar.getInstance().timeInMillis,null, 9.87, "р"),
            SmartRecord("Бананы", Calendar.getInstance().timeInMillis,null, 2.19, "р"),
            SmartRecord("Хлеб", Calendar.getInstance().timeInMillis,null, 0.79, "р"),
            SmartRecord("Рис", Calendar.getInstance().timeInMillis,null, 1.2, "р"),
            SmartRecord("Молоко", Calendar.getInstance().timeInMillis,null, 1.1, "р"),
            SmartRecord("Оливки", Calendar.getInstance().timeInMillis,null, 1.79, "р"),
            SmartRecord("Мука", Calendar.getInstance().timeInMillis,null, 2.44, "р"),
            SmartRecord("Пельмени", Calendar.getInstance().timeInMillis,null, 3.17, "р"),
            SmartRecord("Старка", Calendar.getInstance().timeInMillis,null, 9.87, "р")
        ))

        binding.list.layoutManager = LinearLayoutManager(context)

        binding.list.hasFixedSize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}