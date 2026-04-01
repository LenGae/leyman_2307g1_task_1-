package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.db.DepositDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.viewmodel.MainViewModelFactory

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            DepositRepository(
                DepositDatabase.getDatabase(requireContext()).depositDao()
            )
        )
    }

    companion object {
        fun newInstance() = ResultFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSummary = view.findViewById<TextView>(R.id.tv_summary)
        val btnSave = view.findViewById<Button>(R.id.btn_save)
        val btnHome = view.findViewById<Button>(R.id.btn_home)

        tvSummary.text = mainViewModel.getCalculationSummary()

        btnSave.setOnClickListener {
            mainViewModel.saveCalculation()
        }

        btnHome.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}