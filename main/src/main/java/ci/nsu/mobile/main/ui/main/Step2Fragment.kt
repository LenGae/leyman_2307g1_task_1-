package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ci.nsu.mobile.main.R
import com.google.android.material.textfield.TextInputLayout
import ci.nsu.mobile.main.viewmodel.DepositViewModel

class Step2Fragment : Fragment(R.layout.fragment_step2) {

    private val depositViewModel: DepositViewModel by activityViewModels()

    companion object {
        fun newInstance() = Step2Fragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.spinner_interest)
        val etTopUp = view.findViewById<EditText>(R.id.et_monthly_topup)
        val tilTopUp = view.findViewById<TextInputLayout>(R.id.til_monthly_topup)
        val btnBack = view.findViewById<Button>(R.id.btn_back)
        val btnCalculate = view.findViewById<Button>(R.id.btn_calculate)

        // Доступные ставки
        val period = depositViewModel.periodMonths.value ?: 0
        val rates = when {
            period < 6 -> listOf("15")
            period in 6..11 -> listOf("10")
            period >= 12 -> listOf("5")
            else -> listOf("5","10","15")
        }
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, rates)

        btnCalculate.setOnClickListener {
            val interest = spinner.selectedItem.toString().toDoubleOrNull()
            val topUp = etTopUp.text.toString().toDoubleOrNull() ?: 0.0

            if (interest == null) {
                tilTopUp.error = "Выберите процент"
                return@setOnClickListener
            } else tilTopUp.error = null

            depositViewModel.setInterestRate(interest)
            depositViewModel.setMonthlyTopUp(topUp)
            depositViewModel.calculateFinalAmount()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ResultFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}