package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.db.DepositDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.databinding.FragmentHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val repository by lazy {
        val dao = DepositDatabase.getDatabase(requireContext()).depositDao()
        DepositRepository(dao)
    }

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Подписка на историю депозитов
        lifecycleScope.launch {
            viewModel.history.collectLatest { list ->
                val items = list.map { deposit ->
                    val date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        .format(Date(deposit.calculationDate))
                    "Дата: $date | Старт: ${deposit.initialAmount} | Итог: ${deposit.finalAmount}"
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    items
                )
                binding.lvHistory.adapter = adapter
            }
        }

        // Обработка клика по элементу списка
        binding.lvHistory.setOnItemClickListener { _, _, position, _ ->
            val selectedDeposit = viewModel.history.value.getOrNull(position)
            selectedDeposit?.let {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        ResultFragment.newInstance(it) // Нужно создать этот метод
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}