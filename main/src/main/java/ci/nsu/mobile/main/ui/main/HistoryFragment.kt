package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ci.nsu.mobile.main.R

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var lvHistory: ListView

    private val depositViewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lvHistory = view.findViewById(R.id.lv_history)

        val items = mutableListOf<String>()
        if (depositViewModel.finalAmount > 0) {
            items.add(
                "Старт: ${depositViewModel.initialAmount} | Итог: ${depositViewModel.finalAmount}"
            )
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            items
        )
        lvHistory.adapter = adapter

        lvHistory.setOnItemClickListener { _, _, position, _ ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ResultFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}