package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DepositRepository) : ViewModel() {

    var initialAmount: Double = 0.0
    var periodMonths: Int = 0
    var interestRate: Double = 0.0
    var monthlyTopUp: Double = 0.0
    var finalAmount: Double = 0.0
    var interestEarned: Double = 0.0

    fun calculateFinalAmount() {
        var amount = initialAmount
        for (i in 1..periodMonths) {
            val interest = amount * (interestRate / 100)
            amount += interest + monthlyTopUp
        }
        finalAmount = amount
        interestEarned = finalAmount - initialAmount - monthlyTopUp * periodMonths
    }

    fun getCalculationSummary(): String {
        return """
            Стартовый взнос: $initialAmount
            Срок вклада: $periodMonths месяцев
            Процентная ставка: $interestRate%
            Ежемесячное пополнение: $monthlyTopUp
            Итоговая сумма: $finalAmount
            Начисленные проценты: $interestEarned
        """.trimIndent()
    }

    fun saveCalculation() {
        val deposit = DepositEntity(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.insertDeposit(deposit)
        }
    }
}